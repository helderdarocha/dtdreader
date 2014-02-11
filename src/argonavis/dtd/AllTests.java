package argonavis.dtd;

import junit.framework.*;

public class AllTests {
    
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite testSuite = new TestSuite("Run all tests for DTD Reader");
        
        // sub-package batch tests
        testSuite.addTest(argonavis.dtd.parsers.AllTests.suite());
        testSuite.addTest(argonavis.dtd.tagdata.AllTests.suite());
        
        // this package´s tests
        testSuite.addTest(ElementTagTest.suite());
        testSuite.addTest(NotationTagTest.suite());
        testSuite.addTest(EntityTagTest.suite());
        testSuite.addTest(ExternalGeneralEntityTagTest.suite());
        testSuite.addTest(ExternalParameterEntityTagTest.suite());
        
        testSuite.addTest(GeneralEntityTagTest.suite());
        testSuite.addTest(UnparsedExternalEntityTagTest.suite());
        testSuite.addTest(ParameterEntityTagTest.suite());
        testSuite.addTest(AttlistTagTest.suite());
        testSuite.addTest(AttributeTest.suite());
        
        testSuite.addTest(CommentTagTest.suite());
        testSuite.addTest(ConditionalInclusionTagTest.suite());
        testSuite.addTest(ProcessingInstructionTagTest.suite());
        testSuite.addTest(DTDReaderTest.suite());
        testSuite.addTest(ElementTest.suite());

        return testSuite;
    }
}
