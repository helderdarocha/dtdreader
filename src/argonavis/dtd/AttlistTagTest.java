package argonavis.dtd;

import junit.framework.*;

import argonavis.dtd.tagdata.*;
import argonavis.dtd.parsers.*;
import java.util.*;

public class AttlistTagTest extends TestCase {
    
    public AttlistTagTest(java.lang.String testName) {
        super(testName);
    }
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    public static Test suite() {
        TestSuite suite = new TestSuite(AttlistTagTest.class);
        return suite;
    }

    private String[] tagStrings, elementNames;
    private Attribute[][] testAtts;
    private AttlistTag[] tags;
    
    SortedSet[] attributeSet;
    
    public void setUp() {
        elementNames = new String[] {"elephant", "chimp", "chimp", "chimp","chimp"};
        // result strings (sorted and indented)
        tagStrings = new String[] {
                                    "<!ATTLIST elephant country NOTATION (sa|in) #REQUIRED" +
                                  "\n                   name CDATA #REQUIRED" +
                                  "\n                   parents IDREFS #REQUIRED" +
                                  "\n                   photo ENTITY #REQUIRED" +
                                  "\n                   sex (male|female) #IMPLIED" +
                                  "\n                   species (african|indian) \"african\"" +
                                  "\n                   status CDATA #FIXED \"endangered\"" +
                                  "\n                   weight NMTOKEN #IMPLIED>",  // one
                                    "<!ATTLIST chimp name CDATA #REQUIRED>",     // two
                                    "<!ATTLIST chimp sex (male|female) #IMPLIED>", // three
                                    "<!ATTLIST chimp species (paniscus|troglodytes|homo) \"paniscus\">",   // four
                                    "<!ATTLIST chimp id ID #REQUIRED" +
                                  "\n                park IDREF #REQUIRED" +
                                  "\n                status CDATA #FIXED \"endangered\">"}; // five

        String[] notations = {"sa", "in"};
        String[] notationValues = {"South Africa", "India"};
        
        NotationTagParser notationParser = NotationTagParser.getInstance();
        for (int i = 0; i < notations.length; i++) {
            notationParser.addNotation(new NotationTag(notations[i], notationValues[i]));
        }                          
                                  
        testAtts = new Attribute[tagStrings.length][];                                   
        testAtts[0] = new Attribute[] {
            new Attribute("name", AttributeType.CDATA, DefaultValueDeclaration.REQUIRED),
            new Attribute("sex", new AttributeEnumeration(new String[]{"male", "female"}), 
                                             DefaultValueDeclaration.IMPLIED),
            new Attribute("parents", AttributeType.IDREFS, DefaultValueDeclaration.REQUIRED),
            new Attribute("weight", AttributeType.NMTOKEN, DefaultValueDeclaration.IMPLIED),
            new Attribute("species", new AttributeEnumeration(new String[]{"african", "indian"}), 
                                                 new DefaultValueDeclaration("african")),
            new Attribute("status", AttributeType.CDATA, new DefaultValueDeclaration("endangered", true)),
            new Attribute("country", new AttributeNotationEnumeration(new String[]{"sa", "in"}), 
                                                 DefaultValueDeclaration.REQUIRED),
            new Attribute("photo", AttributeType.ENTITY, DefaultValueDeclaration.REQUIRED),
        };
        testAtts[1] = new Attribute[] {
            new Attribute("name", AttributeType.CDATA, DefaultValueDeclaration.REQUIRED)
        };
        testAtts[2] = new Attribute[] {
            new Attribute("sex", new AttributeEnumeration(new String[]{"male", "female"}), DefaultValueDeclaration.IMPLIED)
        };
        testAtts[3] = new Attribute[] {
            new Attribute("species", new AttributeEnumeration(new String[]{"paniscus", "troglodytes", "homo"}), 
                                              new DefaultValueDeclaration("paniscus"))
        };
        testAtts[4] = new Attribute[] {
            new Attribute("id", AttributeType.ID, DefaultValueDeclaration.REQUIRED),
            new Attribute("park", AttributeType.IDREF, DefaultValueDeclaration.REQUIRED),
            new Attribute("status", AttributeType.CDATA, new DefaultValueDeclaration("endangered", true))
        };
        
        // Attlist now uses Sets instead of arrays - conversion
        attributeSet = new SortedSet[testAtts.length];
        for (int i = 0; i < attributeSet.length; i++) {
            attributeSet[i] = new TreeSet();
            for (int j = 0; j < testAtts[i].length; j++) {
                attributeSet[i].add(testAtts[i][j]);
            }
        }
        
        tags = new AttlistTag[testAtts.length];
        for (int i = 0; i < tags.length; i++) {
            tags[i] = new AttlistTag(elementNames[i], attributeSet[i]);
        }
        
        
    }
    /////////////////

    public void testGetElementName() {
        for (int i = 0; i < tags.length; i++) {
            String result = tags[i].getElementName();
            assertEquals(result, elementNames[i]);
        }
    }
    
    // SortedSet getAttributes()
    public void testGetAttributes() {
        for (int i = 0; i < tags.length; i++) {
            SortedSet result = tags[i].getAttributes();
            assertEquals(result, attributeSet[i]);
        }
    }

    public void testToString() {
        for (int i = 0; i < tags.length; i++) {
            String result = tags[i].toString();
            assertEquals(result, tagStrings[i]);
        }
    }
    
    public void testPrintAttributeList() {
        for (int i = 0; i < tags.length; i++) {
            String result = tags[i].printAttributeList();
            assertEquals("<!ATTLIST " + elementNames[i] + " " + result + ">", tagStrings[i]);
        }
    }
    
    public void testEquals() {
        for (int i = 0; i < tags.length; i++) {
            AttlistTag tag = new AttlistTag("elephant", attributeSet[0]);
            assertEquals(tag, tags[0]);
        }
    }
    
}
