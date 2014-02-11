package argonavis.dtd;

import junit.framework.*;

import argonavis.dtd.parsers.NotationTagParser;

public class UnparsedExternalEntityTagTest extends TestCase {
    
    public UnparsedExternalEntityTagTest(java.lang.String testName) {
        super(testName);
    }
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    public static Test suite() {
        TestSuite suite = new TestSuite(UnparsedExternalEntityTagTest.class);
        return suite;
    }
    
    private String string;
    private UnparsedExternalEntityTag tag;

    public void setUp() {
        tag = new UnparsedExternalEntityTag("icon", "/images/logo32.gif", "gif");
        
        // Set up the following Notation: <!NOTATION gif SYSTEM "image/gif" >
        NotationTagParser parser = NotationTagParser.getInstance();
        parser.addNotation(new NotationTag("gif", "image/gif"));
        
        // Expected string
        string = "<!ENTITY icon SYSTEM \"/images/logo32.gif\" NDATA gif>";
    }
    
    public void testGetValue() {
        String result = tag.getValue(); 
        assertEquals(result, "/images/logo32.gif");
    }
    
    public void testGetName() {
        String result = tag.getName(); 
        assertEquals(result, "icon");
    }

    public void testGetNotationName() {
        String result = tag.getNotationName(); 
        assertEquals(result, "gif");
    }
    
    public void testGetNotationValue() throws ParseException {
        String result = tag.getNotationValue(); 
        assertEquals(result, "image/gif");
    }
    
    public void testToString() {
        String result = tag.toString();
        assertEquals(result, string);
    }
    
    public void testEquals () {
        assertEquals(tag, new UnparsedExternalEntityTag("icon", "/images/logo32.gif", "gif"));
    }
}
