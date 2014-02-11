package argonavis.dtd;

import junit.framework.*;

/**
 * Represents a parsed DTD entity tag
 */
public class ParameterEntityTagTest extends TestCase {
    
    public ParameterEntityTagTest(java.lang.String testName) {
        super(testName);
    }
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    public static Test suite() {
        TestSuite suite = new TestSuite(ParameterEntityTagTest.class);
        return suite;
    }
    
    ParameterEntityTag tag;
    
    public void setUp() {
        tag = new ParameterEntityTag("direction", "(north|south|east|west)");
    }
    
    public void testGetValue() {
        String result = tag.getValue(); 
        assertEquals(result, "(north|south|east|west)");
    }
    
    public void testGetName() {
        String result = tag.getName(); 
        assertEquals(result, "direction");
    }
    
    public void testToString() {
        String expected = "<!ENTITY % direction \"(north|south|east|west)\" >";
        String result = tag.toString();
        assertEquals(result, expected);
    }
    
    public void testEquals () {
        ParameterEntityTag clone = new ParameterEntityTag("direction", "(north|south|east|west)");
        assertEquals(tag, clone);
    }
}
