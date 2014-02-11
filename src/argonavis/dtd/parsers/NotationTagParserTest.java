package argonavis.dtd.parsers;

import argonavis.dtd.*;
import java.util.*;

import junit.framework.*;

public class NotationTagParserTest extends TestCase {
    
    public NotationTagParserTest(String testName) {
        super(testName);
    }
    
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(NotationTagParserTest.class);
        return suite;
    }
    
     NotationTagParser parser;
     String[] testStrings, names;
     NotationTag[] tags;

     public void setUp() {
         parser = NotationTagParser.getInstance();
         
         names = new String[] {"sa", "in", "testSystem", "testPublic", "svg"};
         
         testStrings = new String[] {
             "sa SYSTEM \"South Africa\"",
             "in SYSTEM \"India\"",
             "testSystem    SYSTEM   \"http://www.somewhere.com.br/someplace\"",
             "testPublic    PUBLIC  \"-//ACME Incorporated.//DTD COYOTE 1.0//EN\"",
             "svg SYSTEM \"image/svg\""
         };
             
         tags = new NotationTag[] {
             new NotationTag("sa", "South Africa"),
             new NotationTag("in", "India"),
             new NotationTag("testSystem", "http://www.somewhere.com.br/someplace", NotationTag.SYSTEM_ID),
             new NotationTag("testPublic", "-//ACME Incorporated.//DTD COYOTE 1.0//EN", NotationTag.PUBLIC_ID),
             new NotationTag("svg", "image/svg")
         };
     }
    
    
    public void testGetNotationTable() {
        assertNotNull(parser.getNotationTable());
    }
    
    public void testAddNotation() {
        for (int i = 0; i < tags.length; i++) {
            parser.addNotation(tags[i]);
        }
        HashMap map = parser.getNotationTable();
        for (int i = 0; i < names.length; i++) {
            assertTrue(map.containsKey(names[i]));
            assertTrue(map.containsValue(tags[i]));
        }
    }
    
    /**
     * Returns the value and type (NotationTag object) for a supplied notation name. 
     * Throws an exception if value is not found.
     */
    public void testResolveNotation() throws NotationNotFoundException {
        testAddNotation(); // adds necessary notations
        for (int i = 0; i < names.length; i++) {
            String value = parser.resolveNotation(names[i]);
            assertEquals(value, tags[i].getValue());
        }
    }
    
    /**
    *  @return Returns an NotationTag object
     * @param tagContents Receives the content of an element tag which does NOT include the
     * string "&lt;!NOTATION" nor "&gt;" but all the text in between.
     * This method parses and adds notations to the HashMap.
     */
     // public NotationTag parse(String tagContents)
    public void testParse() throws ParseException {
        for (int i = 0; i < testStrings.length; i++) {
            assertEquals(tags[i], parser.parse(testStrings[i]));
        }
        // Now check if notations were added to HashMap
        HashMap map = parser.getNotationTable();
        for (int i = 0; i < names.length; i++) {
            assertTrue(map.containsKey(names[i]));
            assertTrue(map.containsValue(tags[i]));
        }
    }
    
}
