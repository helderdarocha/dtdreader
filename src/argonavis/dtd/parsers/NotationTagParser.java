package argonavis.dtd.parsers;

import argonavis.dtd.*;
import java.util.*;

public class NotationTagParser {
    
    private static NotationTagParser instance = null;
    
    // this stores all notations read so far (necessary for parsing of other elements)
    private HashMap notationTable = new HashMap();

    public static NotationTagParser getInstance() {
        if (instance == null) {
            instance = new NotationTagParser();
        }
        return instance;
    }
 
    private NotationTagParser() {}
    
    public HashMap getNotationTable() {
        return notationTable;
    }
    
    public void addNotation(NotationTag notation) {
        notationTable.put(notation.getName(), notation);
    }
    
    /**
     * Returns the value and type (NotationTag object) for a supplied notation name. 
     * Throws an exception if value is not found.
     */
    public String resolveNotation(String name) throws NotationNotFoundException {
        NotationTag tag = (NotationTag)notationTable.get(name);
        if (tag == null) {
            throw new NotationNotFoundException("No notation matching '" + name + "' was found.");
        }
        return tag.getValue();
    }
    
    /**
    *  @return Returns an NotationTag object
     * @param tagContents Receives the content of an element tag which does NOT include the
     * string "&lt;!NOTATION" nor "&gt;" but all the text in between.
     */
    public NotationTag parse(String tagContents) throws ParseException {
        if ( tagContents.startsWith("<")  || tagContents.endsWith(">")  || tagContents.startsWith("!NOTATION") ) {
            throw new ParseException("Contents of pre-parsed DTD <!NOTATION> tag may not contain <, > or !NOTATION.");  
        }
        StringTokenizer tokens = new StringTokenizer(tagContents.trim(), " \n\t\r\f(");
        String notationName = tokens.nextToken().trim();
        
        String sourceType = tokens.nextToken().trim();
        int source = 0;
        if ( sourceType.equals("SYSTEM") ) {
            source = NotationTag.SYSTEM_ID;
        } else if ( sourceType.equals("PUBLIC") ) {
            source = NotationTag.PUBLIC_ID;
        } else {
            throw new ParseException ("Unsupported notation source identifier type: " + sourceType + ". Supported id types are SYSTEM and PUBLIC.");
        }

        String value = tokens.nextToken("").trim(); // rest of tag
        
        if (!((value.startsWith("\"") && value.endsWith("\"")) || 
              (value.startsWith("'")  && value.endsWith("'"))))  {
            throw new ParseException ("Notation value must be enclosed in quotation marks or apostrophes: " + value + ".");
        }
        NotationTag tag = new NotationTag(notationName, value.substring(1, value.length()-1), source);
        addNotation(tag);
        return tag;
    }
    
}
