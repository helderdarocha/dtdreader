package argonavis.dtd;

import junit.framework.*;

public class NotationTagTest extends TestCase {
    
    public NotationTagTest(java.lang.String testName) {
        super(testName);
    }
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    public static Test suite() {
        TestSuite suite = new TestSuite(NotationTagTest.class);
        return suite;
    }

    NotationTag[] tags = new NotationTag[3];
    
    public void setUp() {
        tags[0] = new NotationTag("testSystem", "http://www.somewhere.com.br/someplace", NotationTag.SYSTEM_ID);
        tags[1] = new NotationTag("testPublic", "-//ACME Incorporated.//DTD COYOTE 1.0//EN", NotationTag.PUBLIC_ID);
        tags[2] = new NotationTag("svg", "image/svg");
    }
    
    public void testGetSourceIDType() {
        String[] expected = {"SYSTEM", "PUBLIC", "SYSTEM"};
        for (int i = 0; i < tags.length; i++) {
            String received = tags[i].getSourceIDType();
            assertEquals(received, expected[i]);
        }
    }
    
    public void testGetValue() {
        String[] expected = {"http://www.somewhere.com.br/someplace", "-//ACME Incorporated.//DTD COYOTE 1.0//EN", "image/svg"};
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], tags[i].getValue());
        }
    }
    
    public void testToString() {
        String[] expected = {"<!NOTATION testSystem SYSTEM \"http://www.somewhere.com.br/someplace\">",
                             "<!NOTATION testPublic PUBLIC \"-//ACME Incorporated.//DTD COYOTE 1.0//EN\">",
                             "<!NOTATION svg SYSTEM \"image/svg\">"};
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], tags[i].toString());
        }
    }
    
    // Tag equality is string equality
    public void testEquals () {
        NotationTag tagZero = new NotationTag("testSystem", "http://www.somewhere.com.br/someplace", NotationTag.SYSTEM_ID);
        NotationTag shortTagZero = new NotationTag("testSystem", "http://www.somewhere.com.br/someplace");
        assertEquals(tagZero, tags[0]);
        assertEquals(shortTagZero, tags[0]);
    }
    
}
