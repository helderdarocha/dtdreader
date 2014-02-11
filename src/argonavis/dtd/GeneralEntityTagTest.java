package argonavis.dtd;

import junit.framework.*;

public class GeneralEntityTagTest extends TestCase {

    public GeneralEntityTagTest(java.lang.String testName) {
        super(testName);
    }
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    public static Test suite() {
        TestSuite suite = new TestSuite(GeneralEntityTagTest.class);
        return suite;
    }
    
    private GeneralEntityTag tag;
    private String string;
    
    public void setUp() {
        tag = new GeneralEntityTag("copy", 
                                           "<p class=&quot;copyright&quot;>Copyright 2002, Confusing Technologies Inc.</p>");
        string = "<!ENTITY copy \"<p class=&quot;copyright&quot;>Copyright 2002, Confusing Technologies Inc.</p>\">";
    }
    
    public void testGetValue() {
        String result = tag.getValue(); 
        assertEquals(result, "<p class=&quot;copyright&quot;>Copyright 2002, Confusing Technologies Inc.</p>");
    }
    
    public void testGetName() {
        String result = tag.getName(); 
        assertEquals(result, "copy");
    }
    
    public void testToString() {
        String result = tag.toString();
        assertEquals(result, string);       
    }
    
    public void testEquals () {
        assertEquals(tag, new GeneralEntityTag("copy", "<p class=&quot;copyright&quot;>Copyright 2002, Confusing Technologies Inc.</p>"));
    }
}
