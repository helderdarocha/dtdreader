package argonavis.dtd;

import junit.framework.*;

public class ProcessingInstructionTagTest  extends TestCase {

    public ProcessingInstructionTagTest(java.lang.String testName) {
        super(testName);
    }
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    public static Test suite() {
        TestSuite suite = new TestSuite(ProcessingInstructionTagTest.class);
        return suite;
    }
    
    private ProcessingInstructionTag tag;
    private String contents = "Processing instruction=\"It may\" include ?commands* x <<< y <markup>, %entities; &and; \'quotes\'";
    private String name = "command";
    
    public void setUp() {
        tag = new ProcessingInstructionTag(name, contents);
    }
    
    public void testConstructor() {
        assertNotNull(tag);
    }
    
    public void testGetName() {
        assertEquals(name, tag.getName());
    }
    
    public void testGetContents() {
        assertEquals(contents, tag.getContents());
    }
    
    public void testToString() {
        String expected = "<?" + name + " " + contents + " ?>";;
        assertEquals(expected, tag.toString());
    }
    
    public void testEquals () {
        assertEquals(tag, 
        new ProcessingInstructionTag("command", "Processing instruction=\"It may\" include ?commands* x <<< y <markup>, %entities; &and; \'quotes\'"));
    }
}
