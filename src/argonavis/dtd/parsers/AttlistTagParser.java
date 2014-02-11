package argonavis.dtd.parsers;

import argonavis.dtd.*;
import java.util.*;

public class AttlistTagParser {
    
    private static AttlistTagParser instance = null;
    
    private AttlistTagParser() {}
    
    /**
     * Returns a single instance of this parser
     */
    public static AttlistTagParser getInstance() {
        if (instance == null) {
            instance = new AttlistTagParser();
        }
        return instance;
    }
    
    // this stores all attribute lists read so far (so elements can look them up)
    private HashMap attributeSetTable = new HashMap();

    public HashMap getAttributeSetTable() {
        return attributeSetTable;
    }
    
    public void addAttributesToSet(AttlistTag attributeList) {
        String elementName = attributeList.getElementName();
        SortedSet newAttributeSet = attributeList.getAttributes();
        if (attributeSetTable.containsKey(elementName)) {
            SortedSet existingAttributeSet = (SortedSet)attributeSetTable.get(elementName);
            existingAttributeSet.addAll(newAttributeSet);
            newAttributeSet = existingAttributeSet;
        } 
        attributeSetTable.put(elementName, newAttributeSet);
    }
    
    /**
     * Returns and attribute list (array of Attribute) for a supplied element name. 
     * Throws an exception if element name is not found.
     */
    public Attribute[] getAttributes(String elementName) throws ElementNotFoundException {
        SortedSet attributeSet = (SortedSet)attributeSetTable.get(elementName);
        if (attributeSet == null) {
            throw new ElementNotFoundException("No element matching '" + elementName + "' was found.");
        }
        return (Attribute[])attributeSet.toArray(new Attribute[attributeSet.size()]);
    }
    
    /**
    *  @return Returns an AttlistTag object and stores the attributes.
     * @param tagContents Receives the content of an attlist tag which does NOT include the
     * string "&lt;!ATTLIST " nor "&gt;" but all the text in between.
     */
    public AttlistTag parse(String tagContents) throws ParseException {
        
        if ( tagContents.startsWith("<")  || tagContents.endsWith(">")  || tagContents.startsWith("!ATTLIST") ) {
            throw new ParseException("Contents of pre-parsed DTD <!ATTLIST> tag may not start with <, > or !ATTLIST.");  
        }

        StringTokenizer tokens = new StringTokenizer(tagContents.trim(), " \n\t\r\f(");
        String elementName = tokens.nextToken().trim();
        String restOfTag = tokens.nextToken("").trim();
 
        // Parse each attribute
        AttributeParser parser = AttributeParser.getInstance();
        // split string into individual attribute parts
        String[] attributeStrings = splitAttributeString(restOfTag);
        // create Attribute objects by parsing each string
        SortedSet attributeSet = new TreeSet();
        for (int i = 0; i < attributeStrings.length; i++) {
            attributeSet.add(parser.parse(attributeStrings[i]));
        }
        AttlistTag tag = new AttlistTag(elementName, attributeSet);
        addAttributesToSet(tag); // adds these attributes to list
        return tag;
        
    }
    
    public String[] splitAttributeString(String string) throws ParseException {
        // search backwards. If you find a " or a ' it's a boundary
        int apos = string.lastIndexOf("'"); // last apostrophe
        int quot = string.lastIndexOf("\"");// last quote
        int req  = string.lastIndexOf("#REQUIRED"); // last #REQUIRED declaration
        int imp  = string.lastIndexOf("#IMPLIED"); // last #REQUIRED declaration
        // the larger value is the closest boundary
        int lastChar = (int)Math.max(imp + "#IMPLIED".length()-1, Math.max(req + "#REQUIRED".length()-1, Math.max(quot, apos)));
        if ( (apos+quot+req+imp) < 0 || lastChar != string.length()-1) { // none of the above was found
            throw new ParseException("Malformed <!ATTLIST> tag: " + string);
        }
        int continueIdx = (int)Math.max(imp, Math.max(req, Math.max(quot, apos))) - 1;
        if (lastChar == apos) { // last char was apos, there may be one more
            int firstApos = string.lastIndexOf("'", continueIdx);
            continueIdx = firstApos - 1;
        } else if (lastChar == quot) { // last char was quot, there may be one more
            int firstQuot = string.lastIndexOf("\"", continueIdx);
            continueIdx = firstQuot - 1;
        } 
 
        // look for end of previous attribute: may be ', ", #REQUIRED, #IMPLIED or none (-1)
        apos = string.lastIndexOf("'", continueIdx); 
        quot = string.lastIndexOf("\"", continueIdx);
        req  = string.lastIndexOf("#REQUIRED", continueIdx); 
        imp  = string.lastIndexOf("#IMPLIED", continueIdx); 
        int lastEnd = (int)Math.max(imp + "#IMPLIED".length()-1, Math.max(req + "#REQUIRED".length()-1, Math.max(quot, apos)));
        int begin = 0;
        if (lastEnd > "#REQUIRED".length()) { // largest word
            begin = lastEnd+1;
        }
 
        String lastAttributeString = string.substring(begin, lastChar+1).trim();

        String[] attStrings = new String[0];
        if (begin != 0) {
            attStrings = splitAttributeString(string.substring(0, begin));
        }
 
        if (attStrings.length == 0) {
            return new String[] {lastAttributeString};
        } else {
            String[] atts = new String[attStrings.length + 1];
            System.arraycopy(attStrings, 0, atts, 0, attStrings.length);
            System.arraycopy(new String[] {lastAttributeString}, 0, atts, attStrings.length, 1);
            return atts;
        }
    }
    
}
