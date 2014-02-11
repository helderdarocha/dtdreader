package argonavis.dtd.parsers;

import junit.framework.*;

import argonavis.dtd.*;
import argonavis.dtd.tagdata.*;
import java.io.*;
import java.util.StringTokenizer;

public class ContentModelParserTest extends TestCase {
    
    public ContentModelParserTest(java.lang.String testName) {
        super(testName);
    }
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    public static Test suite() {
        TestSuite suite = new TestSuite(ContentModelParserTest.class);
        return suite;
    }
 
    ContentModelParser parser;

    ContentModelItem childElement;
    ContentModelItem[] models;
    ContentModelItem[][] contents;
    final int TEST_FIXTURES = 6;
    
    public void setUp() {
        
        parser = ContentModelParser.getInstance();
        
        //CUT & PASTE FROM CONTENT_MODEL_TEST
        contents = new ContentModelItem[TEST_FIXTURES][];
        models   = new ContentModel[TEST_FIXTURES];
        // Setting up test fixtures for the following content model:
        // "(one,(two|three+),(((four+|five)?,six?)|seven),(eight,nine))*";
        
        // (1) "one";
        childElement = new ChildElement(new Element("one"));
        
        // (2) "(two|three+)";
        contents[0] = new ContentModelItem[] 
                   {new ChildElement(new Element("two")),
                    new ChildElement(new Element("three"), ContentModelItem.ONE_OR_MORE)};
        models[0] = new ChoiceContentModel(contents[0]);
        
        // (3) "(four+|five)?";
        contents[1] = new ContentModelItem[] 
                   {new ChildElement(new Element("four"), ContentModelItem.ONE_OR_MORE),
                    new ChildElement(new Element("five"))};
        models[1] = new ChoiceContentModel(contents[1], ContentModelItem.ZERO_OR_ONE);
        
        // (4) "(eight,nine)";
        contents[2] = new ContentModelItem[] 
                   {new ChildElement(new Element("eight")),
                    new ChildElement(new Element("nine"))};
        models[2] = new SequenceContentModel(contents[2]);
        
        // (5) "((four+|five)?,six?)";
        contents[3] = new ContentModelItem[] 
                   {models[1],
                    new ChildElement(new Element("six"), ContentModelItem.ZERO_OR_ONE)};
        models[3] = new SequenceContentModel(contents[3]);
        
        // (6) "(((four+|five)?,six?)|seven)";
        contents[4] = new ContentModelItem[] 
                   {models[3],
                    new ChildElement(new Element("seven"))};
        models[4] = new ChoiceContentModel(contents[4]);
        
        // (7) "(one,(two|three+),(((four+|five)?,six?)|seven),(eight,nine))*";
        contents[5] = new ContentModelItem[] {childElement, models[0], models[4], models[2]};
        models[5] = new SequenceContentModel(contents[5], ContentModelItem.ZERO_OR_MORE);
        // END - CUT & PASTE FROM CONTENT_MODEL_TEST
    }

    
    ///////////////////////////

    public void testParse() throws ParseException {
        String testStringOne   = "(one,(two|three+),(((four+|five)?,six?)|seven),(eight,nine))*";
        String withSpaces   = "( one, ( two|three+ ), ((( four+ | five)?, six? ) | seven), (eight, nine))*";
        
        ElementContent resultOne = parser.parse(testStringOne);
        assertEquals(resultOne.toString(), testStringOne);
        
        ElementContent resultTwo = parser.parse(withSpaces);
        assertEquals(resultTwo.toString(), testStringOne);
        
        String testStringThree = "ANY";
        ElementContent resultThree = parser.parse(testStringThree);
        assertEquals(resultThree.toString(), testStringThree);
        
        String testStringFour   = "EMPTY";
        ElementContent resultFour = parser.parse(testStringFour);
        assertEquals(resultFour.toString(), testStringFour);
        
        // test for exceptions!
        
    }
    
    /*
     ContentModelItem testParseItemList(String modelString)
     */
    public void testParseExpression() throws ParseException {
       // without internal repetition suffixes
       String[] noSuffix = {"(a)", "(abc)*", "(((abc)))*", "(abc,(xyz))", "(a|((one),two))",
                            "(one,(two|three|four|five),f,g,h)*", "(zero,((one),and),two)"};
       for (int i = 0; i < noSuffix.length; i++) {
           ContentModelItem result = parser.parseExpression(noSuffix[i]);
           assertEquals(noSuffix[i], result.toString());
       }
       
       // without repetition suffixes for parenthesized expressions (only for elements)
       String[] elemSuffix = {"(a*)", "(abc+)*", "(((abc?)))*", "(abc+,(xyz))", "(a*|((one),two+))",
                            "(one,(two+|three|four*|five),f,g?,h)*", "(zero+,((one),and?),two)"};
       for (int i = 0; i < elemSuffix.length; i++) {
           ContentModelItem result = parser.parseExpression(elemSuffix[i]);
           assertEquals(elemSuffix[i], result.toString());
       }
       
       // with repetition suffixes in all terms
       String[] withSuffix = {"(((abc?)*))*", "(abc+,(xyz)?)",
                              "(a*|((one)*,two+))", "(one,(two+|three|four*|five)?,f,g?,h)*", 
                              "(zero+,(((((one)?)?)?)?,and?),two)"};
       for (int i = 0; i < withSuffix.length; i++) {
           ContentModelItem result = parser.parseExpression(withSuffix[i]);
           assertEquals(withSuffix[i], result.toString());
       }

       // Fixture test case
       String testString = "(one,(two|three+),(((four+|five)?,six?)|seven),(eight,nine))*";
       ContentModelItem expected = models[5];
       ContentModelItem result = parser.parseExpression(testString);
       assertEquals(testString, models[5].toString()); // redundant - this asserts the test is correct.
       assertEquals(result.toString(), models[5].toString()); // this is the true test case
    }
    
    
    public void testTokenizeParentheses() throws ParseException {
        String testString = "abc*,def,(xyz,fgh,(yes|no)*,wer,(kuh|lux))+,sum,sub,(123,456)?,xup";
        String[] expectedResults = {"abc*","def","(xyz,fgh,(yes|no)*,wer,(kuh|lux))+","sum","sub","(123,456)?","xup"};
        String[] results = parser.tokenizeParentheses(testString);
        assertEquals(results.length, expectedResults.length);
        for (int i = 0; i < results.length; i++) {
            assertEquals(results[i], expectedResults[i]);
        }
    }
    
    /**
     * This should go to utilities section 
     */
    public static void testRemoveWhiteSpaces() throws IOException {
        String testString = "(one, ( two | three+) ,      (((four+ |  five)?\n \n,  six?) \t\t|seven) , (\feight, \rnine))*";
        String expectedString = "(one,(two|three+),(((four+|five)?,six?)|seven),(eight,nine))*";
        String results = ContentModelParser.removeWhiteSpaces(testString);
        assertEquals(expectedString, results);
    }
    
}
