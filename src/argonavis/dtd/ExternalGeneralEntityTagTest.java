package argonavis.dtd;

import junit.framework.*;

/**
 * External Parsed General Entity
 */
public class ExternalGeneralEntityTagTest extends TestCase {
    
    public ExternalGeneralEntityTagTest(java.lang.String testName) {
        super(testName);
    }
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    public static Test suite() {
        TestSuite suite = new TestSuite(ExternalGeneralEntityTagTest.class);
        return suite;
    }
    
    private ExternalGeneralEntityTag tag;
    private String string;
    
    public void setUp() {
        tag = new ExternalGeneralEntityTag("stylesheet", "http://www.argonavis.com.br/style/xurg.css");
        string = "<!ENTITY stylesheet SYSTEM \"http://www.argonavis.com.br/style/xurg.css\">";
    }
    
    public void testGetValue() {
        String result = tag.getValue(); 
        assertEquals(result, "http://www.argonavis.com.br/style/xurg.css");
    }
    
    public void testGetName() {
        String result = tag.getName(); 
        assertEquals(result, "stylesheet");
    }
    
    public void testToString() {
        String result = tag.toString();
        assertEquals(result, string);       
    }
    
    public void testEquals () {
        assertEquals(tag, new ExternalGeneralEntityTag("stylesheet", "http://www.argonavis.com.br/style/xurg.css"));
    }
}
