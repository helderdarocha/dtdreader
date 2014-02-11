package argonavis.dtd.tagdata;

import argonavis.dtd.Element;
import junit.framework.*;

public class ChildElementTest extends TestCase { 
    
    public ChildElementTest(java.lang.String testName) {
        super(testName);
    }
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    public static Test suite() {
        TestSuite suite = new TestSuite(ChildElementTest.class);
        return suite;
    }
    
    private Element element;
    private ChildElement one, two, three;
    
    public void setUp() {
        element = new Element("travel", new PCData());
        one   = new ChildElement(element);
        two   = new ChildElement(element, ContentModelItem.ONE);
        three = new ChildElement(element, ContentModelItem.ONE_OR_MORE);
    }
    
    public void testGetElement() {
        assertEquals(element, one.getElement()); 
        assertEquals(element, two.getElement()); 
        assertEquals(element, three.getElement()); 
    }    
    
    public void testToString() {
        assertEquals("travel", one.toString());
        assertEquals("travel", two.toString());
        assertEquals("travel+", three.toString());
    }
    
    public void testEquals() {
        assertEquals(one, two);
        assertTrue(!two.equals(three));
    }

}
