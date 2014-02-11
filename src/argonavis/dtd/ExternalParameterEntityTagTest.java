package argonavis.dtd;

import junit.framework.*;
import java.io.IOException;

public class ExternalParameterEntityTagTest extends TestCase {
    
    public ExternalParameterEntityTagTest(java.lang.String testName) {
        super(testName);
    }
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    public static Test suite() {
        TestSuite suite = new TestSuite(ExternalParameterEntityTagTest.class);
        return suite;
    }
    
    private ExternalParameterEntityTag tag;
    private String string;
    
    public void setUp() {
        tag = new ExternalParameterEntityTag("subdata", "subdata.dtd");
        string = "<!ENTITY % subdata SYSTEM \"subdata.dtd\">";
    }
    
    public void testToString() {
        String result = tag.toString();
        assertEquals(result, string);
    }
    
    public void testGetValue() {
        String result = tag.getValue(); 
        assertEquals(result, "subdata.dtd");
    }
    
    public void testGetName() {
        String result = tag.getName(); 
        assertEquals(result, "subdata");
    }
    
    public void testGetContents() throws IOException {
        String result = tag.getContents(); 
        //String fileContents = DTDLoader.load("subdata.dtd");
        String fileContents = "<!-- This feature is not yet supported -->";
        assertEquals(result, fileContents);
    }

    public void testEquals () {
        assertEquals(tag, new ExternalParameterEntityTag("subdata", "subdata.dtd"));
    }
}
