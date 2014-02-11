package argonavis.dtd;

import junit.framework.*;
import argonavis.dtd.parsers.*;
import argonavis.dtd.tagdata.*;
import java.util.Arrays;

public class ElementTest extends TestCase {
    
    public ElementTest(java.lang.String testName) {
        super(testName);
    }
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    public static Test suite() {
        TestSuite suite = new TestSuite(ElementTest.class);
        return suite;
    }
    
    ElementContent contentModel, mixedModel;
    Element[] elements;
    String[] elementNames = {"asterix","obelix","javalix","mix","animix","emptix"};
    // content model
    String withSpaces   = "( one, ( two|three+ ), ((( four+ | five)?, six? ) | seven), (eight, nine))*";

    public void setUp() {
        // Mixed Content
        String[] children = {"bellatrix", "betelgeuse", "alnitak", "saiph", "rigel", "mintaka"}; 
        ChildElement[] contents = new ChildElement[children.length];
        for (int i = 0; i < children.length; i++) {
            contents[i] = new ChildElement(new Element(children[i]));
        }
        mixedModel = new MixedContent(contents);
        
        // using previously tested parser
        try {
            contentModel = ContentModelParser.getInstance().parse(withSpaces);
        } catch (ParseException e) {
            fail ("Exception: " + e);
        }
        elements = new Element[]
                       {new Element(elementNames[0]), 
                        new Element(elementNames[1], contentModel),
                        new Element(elementNames[2], new PCData()),
                        new Element(elementNames[3], mixedModel),
                        new Element(elementNames[4], new AnyModel()),
                        new Element(elementNames[5], new EmptyModel())};
    }
    
    /////////////////
    //public Attribute[] getAttributes()
    public void testGetAttributes() {
        fail("Not done yet!");
    }
    
    // public Element[] getChildren()
    public void testGetChildren() {
        fail("Not done yet!");
    }
    
    public void testGetName() {
        for (int i = 0; i < elements.length; i++) {
            assertEquals(elements[i].getName(), elementNames[i]);
        }
    }
    
    public void testToString() {
        fail("Not done yet!");
        // not implemented - what should this method print?
        // it should have a getTag() method
        // it should print print only the tag name.
    }
    
    public void testGetElements() {
        Element[] elements = Element.getElements(contentModel);
        assertEquals(elements.length, 9); 
        String[] names = new String[elements.length];
        for (int i = 0; i < elements.length; i++) {
            names[i] = elements[i].getName();
        }
        Arrays.sort(names);
        String[] expected = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
        for (int i = 0; i < names.length; i++) {
            assertEquals(names[i], expected[i]);
        }
        
        // still need to test for EMPTY, ANY, etc.
    }
}
