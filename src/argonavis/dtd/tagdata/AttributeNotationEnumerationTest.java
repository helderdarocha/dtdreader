package argonavis.dtd.tagdata;

import junit.framework.*;
import argonavis.dtd.*;
import argonavis.dtd.parsers.*;

public class AttributeNotationEnumerationTest extends TestCase {

    public AttributeNotationEnumerationTest(String testName) {
       super(testName);
    }
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    public static Test suite() {
        return new TestSuite(AttributeNotationEnumerationTest.class);
    }
    
    String   testString;
    String[] nameArray, valueArray;
    AttributeNotationEnumeration testEnum;
    
    public void setUp() {
        testString  = "NOTATION (alpha|beta|gamma|delta|epsilon)";
        nameArray = new String[]{"alpha", "beta", "gamma", "delta", "epsilon"};
        valueArray = new String[]{"http://alpha.com", "http://beta.com", "http://gamma.com", 
                                  "http://delta.com", "http://epsilon.com"};
        
        testEnum = new AttributeNotationEnumeration(nameArray);
        
        NotationTagParser parser = NotationTagParser.getInstance();
        for (int i = 0; i < nameArray.length; i++) {
            parser.addNotation(new NotationTag(nameArray[i], valueArray[i]));
        }
    }
    /**
     * This method returns the real (translated) enumeration values.
     */
    public void testGetResolvedNames() throws NotationNotFoundException {
        String[] result = testEnum.getResolvedNames();
        assertEquals(result.length, valueArray.length);
        for (int i = 0; i < result.length; i++) {
            assertEquals(result[i], valueArray[i]);
        }
    }
    
    /**
     * These names are not resolved.
     */
    public void testToString() {
        assertEquals(testString, testEnum.toString());
    }
    
    public void testEquals() {
        assertEquals(testEnum, new AttributeNotationEnumeration(new String[]{"alpha", "beta", "gamma", "delta", "epsilon"}));
    }
}
