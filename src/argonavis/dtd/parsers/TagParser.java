package argonavis.dtd.parsers;

import argonavis.dtd.*;
import java.util.HashMap;

public class TagParser {
    
    private static TagParser instance = null;

    public static TagParser getInstance() {
        if (instance == null) {
            instance = new TagParser();
        }
        return instance;
    }
 
    private TagParser() {}
    
    /**
    *  @return Returns a Tag object
     * @param tagContents Receives the content of an element tag (does not
     * include the "&lt;" and "&gt;" delimiter chars, but all that is inside them.
     * Tags that were previously treated include:
     * <ul><li>&lt;!-- comment tags --&gt;]
     *     <li>&lt;? processing instruction tags ?&gt;
     *     <li>&lt;[tag[ pre-processed tags such as INCLUDE and IGNORE ]] &gt;
     *     <li>%entity; Parameter entities
     * </ul>
     */
    public Tag parse(String tagContents) throws ParseException {
        if ( tagContents.startsWith("<")  || tagContents.endsWith(">") ) {
            throw new ParseException("Contents of unparsed DTD tag may not contain < or >: " + tagContents);  
        }
        if ( !tagContents.startsWith("!")) {
            throw new ParseException("<" + tagContents + "> is not a valid DTD tag."); 
        }
        
        
        System.out.println("Tag Contents received from DTDReader: " + tagContents);
        
        
        
        String tagData = tagContents.substring(tagContents.indexOf(' ')).trim();
        
        
        System.out.println("Tag Data: " + tagData);
        
        

        if ( tagContents.startsWith("!ELEMENT") ) {
            return ElementTagParser.getInstance().parse(tagData);
        } else if ( tagContents.startsWith("!ATTLIST") ) {
            return AttlistTagParser.getInstance().parse(tagData);
        } else if ( tagContents.startsWith("!ENTITY") ) {
            return EntityTagParser.getInstance().parse(tagData);
        } else if ( tagContents.startsWith("!NOTATION") ) {
            return NotationTagParser.getInstance().parse(tagData);
        } else {
            throw new ParseException("Unsupported tag in DTD: <" + tagContents + ">.");
        }
    }

}
