package argonavis.dtd.parsers;

import junit.framework.*;

public class AllTests {
    
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite testSuite = new TestSuite("Run all tests for parser classes");
        
        // this package´s tests
        testSuite.addTest(TagParserTest.suite());
        testSuite.addTest(ElementTagParserTest.suite());
        testSuite.addTest(AttributeParserTest.suite());
        testSuite.addTest(EntityTagParserTest.suite());
        testSuite.addTest(NotationTagParserTest.suite());
        
        testSuite.addTest(AttlistTagParserTest.suite());
        testSuite.addTest(ContentModelParserTest.suite());
        
        return testSuite;
    }
}
