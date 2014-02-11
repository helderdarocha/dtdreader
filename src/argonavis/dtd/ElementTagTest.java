package argonavis.dtd;

import junit.framework.*;
import argonavis.dtd.tagdata.*;
import argonavis.dtd.parsers.ContentModelParser;
import java.util.*;

public class ElementTagTest extends TestCase {
    
    public ElementTagTest(java.lang.String testName) {
        super(testName);
    }
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    public static Test suite() {
        TestSuite suite = new TestSuite(ElementTagTest.class);
        return suite;
    }
    
    ElementTag tag; 
    ElementContent contentModel;
    String name = "business-card";
    // use only for parsing (has spaces)
    String modelString = "(name, address, (position | department), telephone+, email*, website?)";

    public void setUp() throws ParseException {
        contentModel = ContentModelParser.getInstance().parse(modelString); // tested method
        tag = new ElementTag(name, contentModel);
    }
    
    public void testGetContentModel() {
        assertEquals(contentModel, tag.getContentModel());
    }
    
    public void testToString() {
        assertEquals("<!ELEMENT " + name + " " + contentModel + ">", tag.toString());
    }

    
    public void testGetName() {
        assertEquals(tag.getName(), name); 
    }
    
    public void testEquals () {
        assertEquals(tag, new ElementTag(name, contentModel));
    }
    
}
