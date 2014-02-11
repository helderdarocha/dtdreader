package argonavis.dtd.tagdata;

import junit.framework.*;

public class AttributeEnumerationTest extends TestCase {
    
    public AttributeEnumerationTest(String testName) {
        super(testName);
    }
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    public static Test suite() {
        return new TestSuite(AttributeEnumerationTest.class);
    }
    
    String   testString, oneString, emptyString;
    String[] testArray, oneArray, emptyArray;
    AttributeEnumeration testEnum, oneEnum, emptyEnum;
    
    public void setUp() {
        testString  = "(alpha|beta|gamma|delta|epsilon)";
        testArray = new String[]{"alpha", "beta", "gamma", "delta", "epsilon"};
        
        oneString = "(alpha)";
        oneArray  = new String[]{"alpha"};
        
        emptyString = "()";
        emptyArray = new String[]{""};   
        
        testEnum = new AttributeEnumeration(testArray);
        oneEnum = new AttributeEnumeration(oneArray);
        emptyEnum = new AttributeEnumeration(emptyArray);
    }

    public void testConstructor() {
        assertNotNull(testEnum);
    }
    
    public void testGetNames() {
        String[] result1 = testEnum.getNames();
        assertEquals(result1, testArray);
        
        String[] result2 = oneEnum.getNames();
        assertEquals(result2, oneArray);
        
        String[] result3 = emptyEnum.getNames();
        assertEquals(result3, emptyArray);
    }
    
    public void testToString() {
        String result1 = testEnum.toString();
        assertEquals(result1, testString);
        
        String result2 = oneEnum.toString();
        assertEquals(result2, oneString);
        
        String result3 = emptyEnum.toString();
        assertEquals(result3, emptyString);
    }
    
    public void testEquals() {
        assertEquals(testEnum, new AttributeEnumeration(new String[]{"alpha", "beta", "gamma", "delta", "epsilon"}));
    }
}
