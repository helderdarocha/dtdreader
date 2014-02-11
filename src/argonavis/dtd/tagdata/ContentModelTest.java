package argonavis.dtd.tagdata;

import junit.framework.*;
import argonavis.dtd.Element;

// this next class only for testEquals
import argonavis.dtd.parsers.ContentModelParser;

/**
 * Turn testEquals() off for refactoring, since it depends on
 * other classes.
 */
public class ContentModelTest extends TestCase {

    public ContentModelTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ContentModelTest.class);
        return suite;
    }

    ChildElement childElement;
    ContentModel[] models;
    ContentModelItem[][] contents;
    final int TEST_FIXTURES = 6;
    
    public void setUp() {
        
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

    }

    public void testGetContents() {
        for (int i = 0; i < models.length; i++) {
            ContentModelItem[] cmis = models[i].getContents();
            assertNotNull(cmis);
            assertEquals(cmis.length, contents[i].length);
            for (int j = 0; j < cmis.length; j++) {
                assertEquals(cmis[j], contents[i][j]);
            }
        }
                
    }
    
    public void testToString() {
        String result = models[5].toString();
        String expected = "(one,(two|three+),(((four+|five)?,six?)|seven),(eight,nine))*";
        assertEquals(result, expected);
    }

    public void testPrintContentsAsString() {
        String result = models[5].printContentsAsString('#');
        String expected = "one#(two|three+)#(((four+|five)?,six?)|seven)#(eight,nine)";
        assertEquals(result, expected);
    }
    
    /**
     * This test relies on the ContentModelParser (if it's tests fail, this may fail)
     */
    public void testEquals() throws argonavis.dtd.ParseException {
        String unparsed = "(one,(two|three+),(((four+|five)?,six?)|seven),(eight,nine))*";
        ContentModelParser parser = ContentModelParser.getInstance();
        ContentModel model = (ContentModel)parser.parse(unparsed);
        assertEquals(model, models[5]);
    }

}

