package argonavis.dtd.parsers;

import argonavis.dtd.*;
import argonavis.dtd.tagdata.*;
import java.util.*;

public class ElementTagParser {
    
    private static ElementTagParser instance = null;

    public static ElementTagParser getInstance() {
        if (instance == null) {
            instance = new ElementTagParser();
        }
        return instance;
    }
 
    private ElementTagParser() {}
    
    private HashMap elementTagTable = new HashMap();
    
    public HashMap getElementTagTable() {
        return elementTagTable;
    }
    
    public void addElementTag(ElementTag elementTag) {
        elementTagTable.put(elementTag.getName(), elementTag);
    }
    
    /**
     * Returns the value and type (ElementTag object) for a supplied notation name. 
     * Throws an exception if value is not found.
     */
    public ElementTag getElementTag(String name) throws ElementNotFoundException {
        ElementTag tag = (ElementTag)elementTagTable.get(name);
        if (tag == null) {
            throw new ElementNotFoundException("No element matching '" + name + "' was found.");
        }
        return tag;
    }
    
    /**
    *  @return Returns an ElementTag object
     * @param tagContents Receives the content of an element tag which does NOT include the
     * string "&lt;!ELEMENT " nor "&gt;" but all the text in between.
     */
    public ElementTag parse(String tagContents) throws ParseException {
        if ( tagContents.startsWith("<")  || tagContents.endsWith(">")  || tagContents.startsWith("!ELEMENT") ) {
            throw new ParseException("Contents of pre-parsed DTD <!ELEMENT> tag may not contain <, > or !ELEMENT.");  
        }
        StringTokenizer tokens = new StringTokenizer(tagContents.trim(), " \n\t\r\f(");
        String elementName = tokens.nextToken().trim();
        String restOfTag = tokens.nextToken("").trim();
        ContentModelParser parser = ContentModelParser.getInstance();
        ElementContent contentModel = parser.parse(restOfTag);
        ElementTag tag = new ElementTag(elementName, contentModel);
        addElementTag(tag);
        return tag;
    }
    
}
