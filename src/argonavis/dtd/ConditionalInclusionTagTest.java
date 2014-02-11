package argonavis.dtd;

import junit.framework.*;

public class ConditionalInclusionTagTest extends TestCase {

    public ConditionalInclusionTagTest(java.lang.String testName) {
        super(testName);
    }
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    public static Test suite() {
        TestSuite suite = new TestSuite(ConditionalInclusionTagTest.class);
        return suite;
    }
    
    private ConditionalInclusionTag igTag, inTag;
    private String igString = "<!ENTITY % phantom 'booo!'>";
    private String inString = "<!ENTITY % taxes 'argh!'>";
    
    public void setUp() {
        igTag = new ConditionalInclusionTag(igString, true); // <![IGNORE[...]]>
        inTag = new ConditionalInclusionTag(inString, false); // <![INCLUDE[...]]>
    }
    
    public void testGetContents() {
        assertEquals(igTag.getContents(), igString);
        assertEquals(inTag.getContents(), inString);
    }
    
    public void testGetAction() {
        assertEquals(igTag.getAction(), "IGNORE");
        assertEquals(inTag.getAction(), "INCLUDE");
    }
    
    public void testToString() {
        assertEquals(igTag.toString(), "<![IGNORE[" + igString + "]]>");
        assertEquals(inTag.toString(), "<![INCLUDE[" + inString + "]]>");
    }
    
    public void testEquals (Object obj) {
        assertEquals(igTag, new ConditionalInclusionTag(igString, true));
        assertEquals(inTag, new ConditionalInclusionTag(inString, false)); 
    }
}
