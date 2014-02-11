package argonavis.dtd.tagdata;

import junit.framework.*;

public class AttributeTypeTest extends TestCase {
    
    public AttributeTypeTest(String testName) {
        super(testName);
    }
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    public static Test suite() {
        return new TestSuite(AttributeTypeTest.class);
    }
    
    AttributeType enumeration;
    AttributeType word;
    
    public void setUp() {
        enumeration = new AttributeEnumeration(new String[]{"a", "b", "c"});
        word = AttributeType.CDATA;
    }
    
    public void testConstructor() {
       assertNotNull(enumeration);
    }
    
    public void testToString() {
        assertEquals("(a|b|c)", enumeration.toString());
        assertEquals("CDATA", word.toString());
    }
    
    public void testEquals() {
        assertEquals(AttributeType.CDATA, AttributeType.CDATA);
        assertEquals(enumeration, new AttributeEnumeration(new String[]{"a", "b", "c"}));
    }
}
