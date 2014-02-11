package argonavis.dtd.parsers;

import junit.framework.*;

import argonavis.dtd.*;
import argonavis.dtd.tagdata.*;
import java.util.*;

public class ElementTagParserTest extends TestCase {
    
    public ElementTagParserTest(java.lang.String testName) {
        super(testName);
    }
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    public static Test suite() {
        TestSuite suite = new TestSuite(ElementTagParserTest.class);
        return suite;
    }
    
    private ElementTagParser parser = ElementTagParser.getInstance();
    private String testString = "body (abc*,def,(xyz,fgh,(yes|no)*,wer,(kuh|lux))+,sum,sub,(123,456)?,xup)*";
    ElementTag testObject;
    
    public void setUp() throws ParseException {
        ContentModelParser cmParser = ContentModelParser.getInstance();
        String cm = "(abc*,def,(xyz,fgh,(yes|no)*,wer,(kuh|lux))+,sum,sub,(123,456)?,xup)*";
        ElementContent contentModel = cmParser.parseExpression(cm);
        testObject = new ElementTag("body", contentModel);
    }
    
    public void testGetElementTagTable() {
        assertNotNull(parser.getElementTagTable());
    }
    
    public void testAddElementTag() {
        parser.addElementTag(testObject);
        HashMap map = parser.getElementTagTable();
        ElementTag result = (ElementTag)map.get("body");
        assertEquals(result, testObject);
    }
    
    /**
     * Returns the value and type (ElementTag object) for a supplied notation name. 
     * Throws an exception if value is not found.
     */
    public void testGetElementTag() throws ElementNotFoundException {
        testAddElementTag(); // first add object
        ElementTag tag = parser.getElementTag("body");
        assertEquals(tag, testObject);
    }
    
    /**
    *  @return Returns an ElementTag object
     * @param tagContents Receives the content of an element tag which does NOT include the
     * string "&lt;!ELEMENT " nor "&gt;" but all the text in between.
       public ElementTag parse(String tagContents) throws ParseException {
     */
    public void testParse() throws ParseException {
        String expectedString = "<!ELEMENT " + testString + ">";
        ElementTag tag = parser.parse(testString);
        // Compare Strings
        assertEquals(tag.toString(), expectedString);
        // Compare objects as well!
        assertEquals(tag, testObject);
    }
    
}
