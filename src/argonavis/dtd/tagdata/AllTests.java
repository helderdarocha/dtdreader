package argonavis.dtd.tagdata;

import junit.framework.*;

public class AllTests {
    
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite testSuite = new TestSuite("Run all tests for DTD tag data classes");
        
        // this package´s tests
        testSuite.addTest(ContentModelTest.suite());
        testSuite.addTest(MixedContentTest.suite());
        testSuite.addTest(AttributeEnumerationTest.suite());
        testSuite.addTest(AttributeNotationEnumerationTest.suite());
        testSuite.addTest(DefaultValueDeclarationTest.suite());
        
        testSuite.addTest(AttributeTypeTest.suite());
        testSuite.addTest(ChildElementTest.suite());
        testSuite.addTest(ContentModelItemTest.suite());
        
        return testSuite;
    }
}
