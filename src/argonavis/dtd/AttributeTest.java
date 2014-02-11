package argonavis.dtd;

import junit.framework.*;
import argonavis.dtd.parsers.NotationTagParser;
import argonavis.dtd.tagdata.*;

public class AttributeTest extends TestCase {
    
    public AttributeTest(String testName) {
        super(testName);
    }
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    public static Test suite() {
        return new TestSuite(AttributeTest.class);
    }
    
    String[] names, strings;
    AttributeType[] types;
    DefaultValueDeclaration[] declarations;
    Attribute[] testAtts;
    
    public void setUp() {
        // toString results
        strings = new String[] {
            "name CDATA #REQUIRED", 
            "sex (male|female) #IMPLIED",
            "parents IDREFS #REQUIRED", 
            "weight NMTOKEN #IMPLIED",
            "species (african|indian) \"african\"", 
            "status CDATA #FIXED \"endangered\"",
            "country NOTATION (sa|in) #REQUIRED", 
            "photo ENTITY #REQUIRED",
            "name CDATA #REQUIRED", 
            "sex (male|female) #IMPLIED", 
            "species (paniscus|troglodytes|homo) \"paniscus\"", 
            "id ID #REQUIRED"
        };

        // getName(0 results
        names = new String[] {
            "name",     "sex",
            "parents",  "weight",
            "species",  "status",
            "country",  "photo",
            "name",     "sex",
            "species",  "id"
        };
        // AttributeType environment set up
        String[] notations = {"sa", "in"};
        String[] notationValues = {"South Africa", "India"};
        
        NotationTagParser parser = NotationTagParser.getInstance();
        for (int i = 0; i < notations.length; i++) {
            parser.addNotation(new NotationTag(notations[i], notationValues[i]));
        }
        
        String[] sexValues = {"male", "female"};
        String[] eleSpValues = {"african", "indian"};
        String[] chiSpValues = {"paniscus", "troglodytes", "homo"};
        // getAttributeType() results
        types = new AttributeType[] {
            AttributeType.CDATA,                         new AttributeEnumeration(sexValues), 
            AttributeType.IDREFS,                        AttributeType.NMTOKEN, 
            new AttributeEnumeration(eleSpValues),       AttributeType.CDATA, 
            new AttributeNotationEnumeration(notations), AttributeType.ENTITY,
            AttributeType.CDATA,                         new AttributeEnumeration(sexValues), 
            new AttributeEnumeration(chiSpValues),       AttributeType.ID
        };
        // getDefaultDeclaration() results
        declarations = new DefaultValueDeclaration[] {
            DefaultValueDeclaration.REQUIRED,        DefaultValueDeclaration.IMPLIED,
            DefaultValueDeclaration.REQUIRED,        DefaultValueDeclaration.IMPLIED,
            new DefaultValueDeclaration("african"),  new DefaultValueDeclaration("endangered", true),
            DefaultValueDeclaration.REQUIRED,        DefaultValueDeclaration.REQUIRED,
            DefaultValueDeclaration.REQUIRED,        DefaultValueDeclaration.IMPLIED,
            new DefaultValueDeclaration("paniscus"), DefaultValueDeclaration.REQUIRED
        };
        // test fixture
        testAtts = new Attribute[names.length];
        for (int i = 0; i < testAtts.length; i++) {
            testAtts[i] = new Attribute(names[i], types[i], declarations[i]);
        }
    }
    
    public void testGetName() {
        for (int i = 0; i < testAtts.length; i++) {
            assertEquals(names[i], testAtts[i].getName());
        }
    }
    
    public void testGetAttributeType() {
        for (int i = 0; i < testAtts.length; i++) {
            assertEquals(types[i], testAtts[i].getAttributeType());
        }
    }
    
    public void testGetDefaultDeclaration() {
        for (int i = 0; i < testAtts.length; i++) {
            assertEquals(declarations[i], testAtts[i].getDefaultDeclaration());
        }
    }
    
    public void testToString() {
        for (int i = 0; i < testAtts.length; i++) {
            assertEquals(strings[i], testAtts[i].toString());
        }
    }
    
    public void testHashCode() {
        Attribute[] testAtts2 = new Attribute[names.length];
        for (int i = 0; i < testAtts2.length; i++) {
            // equality for each term should be tested in separate class
            testAtts2[i] = new Attribute(names[i], types[i], declarations[i]);
        }
        for (int i = 0; i < testAtts.length; i++) {
            assertEquals(testAtts[i].hashCode(), testAtts2[i].hashCode());
        }
    }
    
    public void testEquals() {
        Attribute[] testAtts2 = new Attribute[names.length];
        for (int i = 0; i < testAtts2.length; i++) {
            // equality for each term should be tested in separate class
            testAtts2[i] = new Attribute(names[i], types[i], declarations[i]);
        }
        for (int i = 0; i < testAtts.length; i++) {
            assertEquals(testAtts[i], testAtts2[i]);
        }
        assertTrue(testAtts[0].equals(testAtts[8])); // same attribute, different elements
        assertTrue(testAtts[1].equals(testAtts[9])); // same attribute, different elements
    }
    
    /**
     * For sorting by attribute name - ignores element
     */
    public void testCompareTo() {
        assertTrue(testAtts[0].compareTo(testAtts[8]) == 0); // same attribute, different elements
        assertTrue(testAtts[1].compareTo(testAtts[9]) == 0); // same attribute, different elements
    }

}
