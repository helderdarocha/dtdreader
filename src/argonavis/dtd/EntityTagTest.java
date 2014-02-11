package argonavis.dtd;

import junit.framework.*;

/**
 * Represents a parsed DTD entity tag
 */
public class EntityTagTest extends TestCase {
    
    public EntityTagTest(java.lang.String testName) {
        super(testName);
    }
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    public static Test suite() {
        TestSuite suite = new TestSuite(EntityTagTest.class);
        return suite;
    }
    
    EntityTag tag;
    
    public void setUp() {
        // Inner class to test protected constructor via super()
        class NewEntityTag extends EntityTag {
            NewEntityTag() {
                super("direction", "(north|south|east|west)");
            }
        }
        tag = new NewEntityTag();
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
        String expected = "<!ENTITY direction \"(north|south|east|west)\">";
        String result = tag.toString();
        assertEquals(result, expected);
    }
    
    public void testEquals () {
        // Inner class to invoke protected constructor via super()
        class TestEntityTag extends EntityTag {
            TestEntityTag(String name, String value) {
                super(name, value);
            }
        }
        // Since we didn't override equals in class above, the objects should compare as equal
        EntityTag clone = new TestEntityTag("direction", "(north|south|east|west)");
        assertEquals(tag, clone);
    }
}
