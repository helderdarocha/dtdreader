package argonavis.dtd;

import junit.framework.*;

public class CommentTagTest extends TestCase {
    
    public CommentTagTest(java.lang.String testName) {
        super(testName);
    }
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    public static Test suite() {
        TestSuite suite = new TestSuite(CommentTagTest.class);
        return suite;
    }
    
    private CommentTag tag;
    private String contents = "This is a comment. It includes <markup>, <!ENTITY valid \"tags\">, %entities; &and; \'quotes\'";
    
    public void setUp() {
        tag = new CommentTag(contents);
    }
    
    public void testConstructor() {
        assertNotNull(tag);
    }
    
    public void testGetContents() {
        assertEquals(contents, tag.getContents());
    }
    
    public void testToString() {
        String expected = "<!-- " + contents + " -->";
        assertEquals(expected, tag.toString());
    }
    
    public void testEquals () {
        assertEquals(tag, new CommentTag("This is a comment. It includes <markup>, <!ENTITY valid \"tags\">, %entities; &and; \'quotes\'"));
    }
}
