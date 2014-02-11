package argonavis.dtd.parsers;

import argonavis.dtd.*;
import argonavis.dtd.tagdata.*;
import java.io.*;
import java.util.*;

import junit.framework.*;

public class AttributeParserTest extends TestCase {
    
    public AttributeParserTest(java.lang.String testName) {
        super(testName);
    }
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    public static Test suite() {
        TestSuite suite = new TestSuite(AttributeParserTest.class);
        return suite;
    }

    AttributeParser parser;
    
    public void setUp() {
        parser = AttributeParser.getInstance();
    }
    

    public void testParse() throws ParseException {
        String elementName = "elephant";
        String[] attStrings = {"name CDATA #REQUIRED",
                               "sex (male | female) #IMPLIED",
                               "parents IDREFS #REQUIRED",
                               "weight NMTOKEN #IMPLIED",
                               "species (african | indian) \"african\"",
                               "status CDATA #FIXED 'endangered'",
                               "country NOTATION (sa | in) #REQUIRED",
                               "photo ENTITY #REQUIRED"};
        
        String[] notations = {"sa", "in"};
        String[] notationValues = {"South Africa", "India"};
        
        NotationTagParser notationParser = NotationTagParser.getInstance();
        for (int i = 0; i < notations.length; i++) {
            notationParser.addNotation(new NotationTag(notations[i], notationValues[i]));
        }
        
        String[] sexValues = {"male", "female"};
        String[] eleSpValues = {"african", "indian"};
        
        Attribute[] atts = {
            new Attribute("name", AttributeType.CDATA, DefaultValueDeclaration.REQUIRED), 
            new Attribute("sex", new AttributeEnumeration(sexValues), DefaultValueDeclaration.IMPLIED), 
            new Attribute("parents", AttributeType.IDREFS, DefaultValueDeclaration.REQUIRED), 
            new Attribute("weight", AttributeType.NMTOKEN, DefaultValueDeclaration.IMPLIED), 
            new Attribute("species", new AttributeEnumeration(eleSpValues), new DefaultValueDeclaration("african")), 
            new Attribute("status", AttributeType.CDATA, new DefaultValueDeclaration("endangered", true)), 
            new Attribute("country", new AttributeNotationEnumeration(notations), DefaultValueDeclaration.REQUIRED), 
            new Attribute("photo", AttributeType.ENTITY, DefaultValueDeclaration.REQUIRED)
        };
        
        for (int i = 0; i < atts.length; i++) {
            assertEquals(atts[i], parser.parse(attStrings[i]));
        }
    }
    
    // String[] parseChoiceList(String string)
    public void testParseEnumeration() throws ParseException {
        // three test strings that should result in the same array
        String[] testStr = {"( one | two | three | four )",
                            "(one|\ntwo\t |   three  \n|four  )",
                            "(one|two|three|four)"};
        String[] expected = {"one", "two", "three", "four"};
        for (int i = 0; i < testStr.length; i++) {
            String[] result = parser.parseEnumeration(testStr[i]);
            assertEquals(expected.length, result.length);
            for (int j = 0; j < result.length; j++) {
                assertEquals(result[j], expected[j]);
            }
        }
    }
    
}
