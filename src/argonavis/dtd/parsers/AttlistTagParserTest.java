package argonavis.dtd.parsers;

import argonavis.dtd.*;
import argonavis.dtd.tagdata.*;
import java.util.*;

import junit.framework.*;

public class AttlistTagParserTest extends TestCase {
    
    public AttlistTagParserTest(java.lang.String testName) {
        super(testName);
    }
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    public static Test suite() {
        TestSuite suite = new TestSuite(AttlistTagParserTest.class);
        return suite;
    }
    
    private AttlistTagParser parser;
    private NotationTagParser notationParser;
    
    private String[] tagContents;
    private Attribute[][] testAtts;
    
    private SortedSet[] attributeSet;
    
    public void setUp() {
        
        parser = AttlistTagParser.getInstance();
        
        String[] notations = {"sa", "in"};
        String[] notationValues = {"South Africa", "India"};
        
        notationParser = NotationTagParser.getInstance();
        for (int i = 0; i < notations.length; i++) {
            notationParser.addNotation(new NotationTag(notations[i], notationValues[i]));
        }

        // a set of pre-parsed strings for testing
        tagContents = new String[] {
                                    "elephant name CDATA #REQUIRED" +
                                          "\n sex (male|female) #IMPLIED" +
                                          "\n parents IDREFS #REQUIRED" +
                                          "\n weight NMTOKEN #IMPLIED" +
                                          "\n species (african|indian) \"african\"" +
                                          "\n status CDATA #FIXED 'endangered'" +
                                          "\n country NOTATION (sa|in) #REQUIRED" +
                                          "\n photo ENTITY #REQUIRED",  // one
                                    "chimp name CDATA #REQUIRED",     // two
                                    "chimp sex (male|female) #IMPLIED", // three
                                    "chimp species (paniscus|troglodytes|homo) 'paniscus'",   // four
                                    "chimp id ID #REQUIRED park IDREF #REQUIRED" +
                                       "\n status CDATA #FIXED \"endangered\""}; // five

        testAtts = new Attribute[tagContents.length][];                                   
        testAtts[0] = new Attribute[] {
            new Attribute("name", AttributeType.CDATA, DefaultValueDeclaration.REQUIRED),
            new Attribute("sex", new AttributeEnumeration(new String[]{"male", "female"}), 
                                             DefaultValueDeclaration.IMPLIED),
            new Attribute("parents", AttributeType.CDATA, DefaultValueDeclaration.REQUIRED),
            new Attribute("weight", AttributeType.CDATA, DefaultValueDeclaration.IMPLIED),
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
            new Attribute("id", AttributeType.CDATA, DefaultValueDeclaration.REQUIRED),
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
        
    }

    public void testGetAttributeListTable() {
        assertNotNull(parser.getAttributeSetTable());
    }
    
    public void testAddAttributesToSet() {
        String[] elementNames = {"elephant", "chimp", "chimp", "chimp","chimp"};
        for (int i = 0; i < elementNames.length; i++) {
            parser.addAttributesToSet(new AttlistTag(elementNames[i], attributeSet[i]));
        }
        // there should be 8 attributes under "elephant" and 6 under "chimp"
        HashMap map = parser.getAttributeSetTable();
        SortedSet elephants = (SortedSet)map.get("elephant");
        String[] eleAtNames = {"name", "sex", "parents", "weight", "species", "status", "country", "photo"};
        Attribute[] eleAttribs = (Attribute[])elephants.toArray(new Attribute[elephants.size()]);
        Arrays.sort(eleAtNames);
        assertEquals(eleAttribs.length, 8);
        for (int i = 0; i < eleAtNames.length; i++) {
            assertEquals(eleAtNames[i], eleAttribs[i].getName());
        }
        
        SortedSet chimps = (SortedSet)map.get("chimp");
        String[] chimpAtNames = {"name", "sex", "species", "id", "park", "status"};
        Attribute[] chimpAttribs = (Attribute[])chimps.toArray(new Attribute[chimps.size()]);
        Arrays.sort(chimpAtNames);
        assertEquals(chimpAttribs.length, 6);
        for (int i = 0; i < chimpAtNames.length; i++) {
            assertEquals(chimpAtNames[i], chimpAttribs[i].getName());
        }
    }
    
    /**
     * Returns and attribute list (array of Attribute) for a supplied element name. 
     * Throws an exception if element name is not found.
     */
    public void testGetAttributes() throws ElementNotFoundException {
        testAddAttributesToSet(); // add necessary data
        Attribute[] elephants = parser.getAttributes("elephant");
        assertEquals(elephants.length, 8);
        String[] eleAtNames = {"name", "sex", "parents", "weight", "species", "status", "country", "photo"};
        Arrays.sort(eleAtNames);
        for (int i = 0; i < eleAtNames.length; i++) {
            assertEquals(eleAtNames[i], elephants[i].getName());
        }
        
        Attribute[] chimps = parser.getAttributes("chimp");
        assertEquals(chimps.length, 6);
        String[] chimpAtNames = {"name", "sex", "species", "id", "park", "status"};
        Arrays.sort(chimpAtNames);
        for (int i = 0; i < chimpAtNames.length; i++) {
            assertEquals(chimpAtNames[i], chimps[i].getName());
        }
    }
    
    public void testParse() throws ParseException {
        String[] elementNames = {"elephant", "chimp", "chimp", "chimp","chimp"};
        for (int i = 0; i < tagContents.length; i++) {
            AttlistTag result = parser.parse(tagContents[i]);
            assertEquals(result, new AttlistTag(elementNames[i], attributeSet[i]));
        }
        // Now check if attributes were added to HashMap correctly:
        // 8 attributes under "elephant" and 6 under "chimp"
        HashMap map = parser.getAttributeSetTable();
        SortedSet elephants = (SortedSet)map.get("elephant");
        String[] eleAtNames = {"name", "sex", "parents", "weight", "species", "status", "country", "photo"};
        Attribute[] eleAttribs = (Attribute[])elephants.toArray(new Attribute[elephants.size()]);
        Arrays.sort(eleAtNames);
        assertEquals(eleAttribs.length, 8);
        for (int i = 0; i < eleAtNames.length; i++) {
            assertEquals(eleAtNames[i], eleAttribs[i].getName());
        }
        
        SortedSet chimps = (SortedSet)map.get("chimp");
        String[] chimpAtNames = {"name", "sex", "species", "id", "park", "status"};
        Attribute[] chimpAttribs = (Attribute[])chimps.toArray(new Attribute[chimps.size()]);
        Arrays.sort(chimpAtNames);
        assertEquals(chimpAttribs.length, 6);
        for (int i = 0; i < chimpAtNames.length; i++) {
            assertEquals(chimpAtNames[i], chimpAttribs[i].getName());
        }
        
        // Extra tests
        String test2 = "image id ID #IMPLIED\n" +
             "                src CDATA #REQUIRED\n" +
             "                type (gif | jpg | png) \"png\"";
        AttlistTag result2 = parser.parse(test2);
        SortedSet set2 = new TreeSet();
        set2.add(new Attribute("id", AttributeType.ID, DefaultValueDeclaration.IMPLIED));
        set2.add(new Attribute("src", AttributeType.CDATA, DefaultValueDeclaration.REQUIRED));
        set2.add(new Attribute("type", new AttributeEnumeration(new String[]{"gif","jpg","png"}), new DefaultValueDeclaration("png")));
        assertEquals(result2, new AttlistTag("image", set2));
    }
    
    public void testGetInstance() {
        AttlistTagParser parser1 = AttlistTagParser.getInstance();
        assertNotNull(parser1);
        AttlistTagParser parser2 = AttlistTagParser.getInstance();
        assertEquals(parser1, parser2);
    }
    
    public void testSplitAttributeString() throws ParseException {
        String[] expected = {"name CDATA #REQUIRED",
                             "sex (male | female) #IMPLIED",
                             "parents IDREFS #REQUIRED",
                             "weight NMTOKEN #IMPLIED", 
                             "species (african | indian) \"african\"", 
                             "status CDATA #FIXED 'endangered'", 
                             "country NOTATION (sa | in) #REQUIRED", 
                             "photo ENTITY #REQUIRED"};
        String supplied =  "name CDATA #REQUIRED\n" +
                           "sex (male | female) #IMPLIED\n" +
                           "parents IDREFS #REQUIRED\n" +
                           "weight NMTOKEN #IMPLIED\n" +
                           "species (african | indian) \"african\"\n" +
                           "status CDATA #FIXED 'endangered'\n" +
                           "country NOTATION (sa | in) #REQUIRED\n" +
                           "photo ENTITY #REQUIRED";
        String[] result = parser.splitAttributeString(supplied);
        assertEquals(result.length, expected.length);
        for (int i = 0; i < result.length; i++) {
            assertEquals(expected[i], result[i]);
        }
        
        // Strange cases: #REQUIRED and #IMPLIED appearing inside quoted strings.
        String[] expectedTwo = {"name CDATA #REQUIRED",
                                "species (african | indian) \"#REQUIRED\"", 
                                "status CDATA #FIXED '#IMPLIED'", 
                                "country NOTATION (sa | in) #REQUIRED"};
        String suppliedTwo =  "name CDATA #REQUIRED\n" +
                              "species (african | indian) \"#REQUIRED\"\n" +
                              "status CDATA #FIXED '#IMPLIED'\n" +
                              "country NOTATION (sa | in) #REQUIRED";
        String[] resultTwo = parser.splitAttributeString(suppliedTwo);
        assertEquals(resultTwo.length, expectedTwo.length);
        for (int i = 0; i < resultTwo.length; i++) {
            assertEquals(expectedTwo[i], resultTwo[i]);
        }
        
        // Extra cases
        String suppliedThree = "id ID #IMPLIED\n" +
               "                src CDATA #REQUIRED\n" +
               "                type (gif | jpg | png) \"png\"";
        String[] expectedThree = {"id ID #IMPLIED",
                                  "src CDATA #REQUIRED",
                                  "type (gif | jpg | png) \"png\""};
        String[] resultThree = parser.splitAttributeString(suppliedThree);
        assertEquals(resultThree.length, expectedThree.length);
        for (int i = 0; i < resultThree.length; i++) {
            assertEquals(expectedThree[i], resultThree[i]);
        }
    }
    
}
