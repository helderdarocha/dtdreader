package argonavis.dtd.tagdata;

import junit.framework.*;

public class DefaultValueDeclarationTest extends TestCase {
    
    public DefaultValueDeclarationTest(String testName) {
       super(testName);
    }
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    public static Test suite() {
        return new TestSuite(DefaultValueDeclarationTest.class);
    }
    
    private DefaultValueDeclaration required, implied, literal, fixed;
    
    public void setUp() {
        required = DefaultValueDeclaration.REQUIRED;
        implied = DefaultValueDeclaration.IMPLIED;
        literal = new DefaultValueDeclaration("one two three");
        fixed = new DefaultValueDeclaration("two://four.six", true);
    }
        
    public void testGetDefaultValue() {
        assertEquals("one two three", literal.getDefaultValue());
        assertEquals("two://four.six", fixed.getDefaultValue());
        
        // these cases should not return null (null is not a default value)
        assertEquals("", required.getDefaultValue());
        assertEquals("", implied.getDefaultValue());
    }
    
    public void testToString() {
        assertEquals(required.toString(), "#REQUIRED");
        assertEquals(implied.toString(), "#IMPLIED");
        assertEquals(literal.toString(), "\"one two three\"");
        assertEquals(fixed.toString(), "#FIXED \"two://four.six\"");
    }
    
    public void testEquals() {
        assertEquals(required, DefaultValueDeclaration.REQUIRED);
        assertEquals(implied, DefaultValueDeclaration.IMPLIED);
        assertEquals(literal, new DefaultValueDeclaration("one two three"));
        assertEquals(fixed, new DefaultValueDeclaration("two://four.six", true));
    }
    
}
