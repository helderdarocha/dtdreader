package argonavis.dtd;

import java.io.*;
import java.util.*;
import argonavis.dtd.parsers.*;

import junit.framework.*;

/**
 * Converts a DTD isto a set of tag arrays.
 * Creates a set of Element arrays from the results.
 */
public class DTDReaderTest extends TestCase {
    
    public DTDReaderTest(java.lang.String testName) {
        super(testName);
    }
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    public static Test suite() {
        TestSuite suite = new TestSuite(DTDReaderTest.class);
        return suite;
    }
    
    private DTDReader reader; 
    private String[] dtdSource;
    private String[][] strings;
    private DTDComponent[][] expected;
    
    public void setUp() throws ParseException {
        
        dtdSource = new String[] {
            "<!ELEMENT sonnet (author,title?,text) >\n" +
            "<!ATTLIST sonnet\n" +
            "          type (Shakespearean | Petrarchan) \"Shakespearean\">   \n" +
            "<!ELEMENT text (line,line,line,line,\n" +
            "                line,line,line,line,\n" +
            "                line,line,line,line,\n" +
            "                line,line) >\n" +
            "<!ELEMENT author (last-name,first-name,\n" +
            "                  year-of-birth?,year-of-death?) >\n" +
            "<!ELEMENT title (#PCDATA)>\n" +
            "<!ELEMENT last-name (#PCDATA)>\n" +
            "<!ELEMENT first-name (#PCDATA)>\n" +
            "<!ELEMENT year-of-birth (#PCDATA)>\n" +
            "<!ELEMENT year-of-death (#PCDATA)>\n" +
            "<!ELEMENT line (#PCDATA)>",
            
            "<!-- Image Sample DTD -->\n" +
            "<!NOTATION gif SYSTEM \"image/gif\" >\n" +
            "<!NOTATION jpg SYSTEM \"image/jpg\" >\n" +
            "<!NOTATION png SYSTEM \"image/png\" >\n" +
            "<!-- Comments may contain <!ELEMENT> or other markup -->\n" +
            "<!ENTITY % icon \"NOTATION (gif | jpg | png)\">\n" +
            "<!ENTITY icon SYSTEM \"/images/logo32.gif\" NDATA gif>\n" +
            "<!ENTITY % data \"title, author, description\" >\n" +
            "<!ENTITY copyright '<p>&copy; 2002 Unlimited \"U\" &amp; &apos;I&apos; Inc.'>\n" +
            "<!ENTITY % subdata SYSTEM \"subdata.dtd\">\n" +
            "<!ENTITY home SYSTEM \"index.html\">\n" +
            "<!ELEMENT image (%data;, (file | link)+)>\n" +
            "<!ATTLIST image id ID #IMPLIED\n" +
            "                src CDATA #REQUIRED\n" +
            "                type %icon; \"png\">\n" +
            "<?xml-stylesheet This is a place-holder. Test < and > and \" and ' ?>\n" +
            "<!ELEMENT title (#PCDATA)>\n" +
            "<!ELEMENT description (#PCDATA)>\n" +
            "<!ELEMENT author (firstname, lastname)>\n" +
            "<!ATTLIST author id ID #IMPLIED>\n" +
            "<!ATTLIST author member CDATA #FIXED \"true\">\n" +
            "<!ELEMENT firstname (#PCDATA)>\n" +
            "<!ELEMENT lastname (#PCDATA)>",
            
            "<!ENTITY black \"noir\">\n" +
            "<!ENTITY % subdtd \"<!ENTITY % white 'blanc'>\">\n" +
          //  "%subdtd;\n" +
            "<!ENTITY % apos '\"'>\n" +
            "<!ENTITY % quotest \"<!ENTITY % red %apos;rouge%apos;><!ELEMENT ghost EMPTY>\">\n" 
          //  "%quotest;\n" +
            //"<!ELEMENT light (%white;)>"
          //  "<!ELEMENT color (%white;, %red;)>"
        };
        
        strings = new String[][] {
            {
                "<!ELEMENT sonnet (author,title?,text)>",
                "<!ATTLIST sonnet\n          type (Shakespearean | Petrarchan) \"Shakespearean\">",
                "<!ELEMENT text (line,line,line,line,\nline,line,line,line,\nline,line,line,line,\nline,line)>",
                "<!ELEMENT author (last-name,first-name,\n                  year-of-birth?,year-of-death?)>",
                "<!ELEMENT title (#PCDATA)>",
                "<!ELEMENT last-name (#PCDATA)>",
                "<!ELEMENT first-name (#PCDATA)>",
                "<!ELEMENT year-of-birth (#PCDATA)>",
                "<!ELEMENT year-of-death (#PCDATA)>",
                "<!ELEMENT line (#PCDATA)>"  
            },
            {
                "<!-- Image Sample DTD -->",
                "<!NOTATION gif SYSTEM \"image/gif\" >",
                "<!NOTATION jpg SYSTEM \"image/jpg\" >",
                "<!NOTATION png SYSTEM \"image/png\" >",
                "<!-- Comments may contain <!ELEMENT> or other markup -->",
                "<!ENTITY % icon \"NOTATION (gif | jpg | png)\">",
                "<!ENTITY icon SYSTEM \"/images/logo32.gif\" NDATA gif>",
                "<!ENTITY % data \"title, author, description\" >",
                "<!ENTITY copyright '<p>&copy; 2002 Unlimited \"U\" &amp; &apos;I&apos; Inc.'>",
                "<!ENTITY % subdata SYSTEM \"subdata.dtd\">",
                "<!ENTITY home SYSTEM \"index.html\">",
                "<!ELEMENT image (title, author, description, (file | link)+)>",
                "<!ATTLIST image id ID #IMPLIED src CDATA #REQUIRED type NOTATION (gif | jpg | png) \"png\">",
                "<?xml-stylesheet This is a place-holder. Test < and > and \" and ' ?>",
                "<!ELEMENT title (#PCDATA)>",
                "<!ELEMENT description (#PCDATA)>",
                "<!ELEMENT author (firstname, lastname)>",
                "<!ATTLIST author id ID #IMPLIED>",
                "<!ATTLIST author member CDATA #FIXED \"true\">",
                "<!ELEMENT firstname (#PCDATA)>",
                "<!ELEMENT lastname (#PCDATA)>"
            },
            {
                "<!ENTITY black \"noir\">",
                "<!ENTITY % subdtd \"<!ENTITY % white 'blanc'>\">",
               // "%subdtd;",
                "<!ENTITY % apos '\"'>",
                "<!ENTITY % quotest \"<!ENTITY % red \"rouge\"><!ELEMENT ghost EMPTY>\">"
              //  "%quotest;",
                //"<!ELEMENT light (blanc)>"
             // "<!ELEMENT color (blanc, rouge)>"
            }
            
            // IF YOU ADD ANY EXTRA DTD WHICH CONTAINS DTD SUBSETS FOR SUBSTITUTION,
            // YOU MUST ADD AN if TERM IN THE LOOP BELOW!
        };
        
        // class below is tested
        TagParser parser = TagParser.getInstance();
        
        expected = new DTDComponent[dtdSource.length][]; // array of DTDs

        for (int i = 0; i < dtdSource.length; i++) {
            List compList = new ArrayList(); // list of DTD Components
            for (int j = 0; j < strings[i].length; j++) {
                String s = strings[i][j].trim();
                if (s.startsWith("<!--")) {
                    compList.add(new CommentTag(s.substring(4, s.length()-3)));
                } else if (s.startsWith("<?")) {
                    int space = s.indexOf(" ");
                    String name = s.substring(2, space);
                    String contents = s.substring(space+1, s.length()-2).trim();
                    compList.add(new ProcessingInstructionTag(name, contents));
                } else if (s.startsWith("<!")) {  
                    compList.add(parser.parse(s.substring(1, s.length()-1)));
                } else if (s.startsWith("%")) { // turn this off while parse is not done
                    if (s.equals("%subdtd;")) {
                        compList.add(parser.parse("!ENTITY % white 'blanc'"));
                    } else if (s.equals("%quotest;")) {
                        compList.add(parser.parse("!ENTITY % red \"rouge\""));
                        compList.add(parser.parse("!ELEMENT ghost EMPTY"));
                    } else if (s.equals("%loadtest;")) {
                        compList.add(parser.parse("!-- External DTD subset --"));
                        compList.add(parser.parse("!ELEMENT quantity ( #PCDATA )"));
                        compList.add(parser.parse("!ATTLIST quantity rule CDATA #REQUIRED"));
                    }                    
                    // ADD EXTRA if-else TERM HERE IF NECESSARY! -----------
                }
            }
            DTDComponent[] comps = (DTDComponent[])compList.toArray(new DTDComponent[compList.size()]);
            expected[i] = comps;
        }
    }

    public void testParse()  throws ParseException {
        for (int i = 0; i < dtdSource.length; i++) {
            DTDComponent[] components = DTDReader.parse(dtdSource[i]);
            assertNotNull(components);
            assertEquals(components.length, expected[i].length);
            for (int j = 0; j < components.length; j++) {
                if (expected[i][j] instanceof AttlistTag) {
                    // different method of comparison - attributes may be merged in one tag or not!
                    // compare separately!
                    System.out.println("Expected: " + expected[i][j]);
                    System.out.println("Received: " + components[j]);
                } else {
                    assertEquals(components[j], expected[i][j]);
                }
            }
        }
    }
    
    public void testResolveEntity() throws EntityNotFoundException, ParseException {
        testGetEntities(); // set some entities
        String[] names = {"j2se", "j2ee", "apos"};
        String[] expected = {"- Java 2 Standard Edition -", "- Java 2 Enterprise Edition -", "\""};
        for (int i = 0; i < names.length; i++) {
            String value = DTDReader.resolveEntity(names[i]);
            assertEquals(expected[i], value);
        }
        
    }
    
    public void testProcessTag() throws ParseException {
        testGetEntities(); // sets some entities;
        String testOne = DTDReader.processTag("this is a %j2se;%j2ee; compiler.");
        String testTwo = DTDReader.processTag("this is a plain string");
        String testThree = DTDReader.processTag("!ENTITY % red %apos;rouge%apos;");
        String expectedOne = "this is a - Java 2 Standard Edition -- Java 2 Enterprise Edition - compiler.";
        String expectedTwo = new String("this is a plain string");
        String expectedThree = new String("!ENTITY % red \"rouge\"");
        assertEquals(testOne, expectedOne);
        assertEquals(testTwo, expectedTwo);
        assertEquals(testThree, expectedThree);
    }
    
    public void testParseComponent() throws ParseException {
        for (int i = 0; i < dtdSource.length; i++) {
            for (int j = 0; j < expected[i].length; j++) {
                if (strings[i][j].startsWith("<")) {
                    DTDComponent component = DTDReader.parseComponent(strings[i][j]);
                    assertEquals(component, expected[i][j]);
                } else {
                    String s = strings[i][j];
                    TagParser parser = TagParser.getInstance();
                    // redundant - just for symmetry
                    if (s.equals("%subdtd;")) {
                        assertEquals(parser.parse("!ENTITY % white 'blanc'"), DTDReader.parseComponent("<!ENTITY % white 'blanc'>") );
                    } else if (s.equals("%quotest;")) {
                        assertEquals(parser.parse("!ENTITY % red \"rouge\""), DTDReader.parseComponent("<!ENTITY % red \"rouge\">") );
                        assertEquals(parser.parse("!ELEMENT ghost EMPTY"), DTDReader.parseComponent("<!ELEMENT ghost EMPTY>") );
                    } else if (s.equals("%loadtest;")) {
                        assertEquals(new CommentTag("!-- External DTD subset --"), DTDReader.parseComponent("<!-- External DTD subset -->") );
                        assertEquals(parser.parse("!ELEMENT quantity ( #PCDATA )"), DTDReader.parseComponent("<!ELEMENT quantity ( #PCDATA )>") );
                        assertEquals(parser.parse("!ATTLIST quantity rule CDATA #REQUIRED"), DTDReader.parseComponent("<!ATTLIST quantity rule CDATA #REQUIRED>") );
                    }
                }
            }
        }
    }
    
    public void testNormalizeSpaces() throws IOException {
        String expected = " a b c d e f g h";
        String result = DTDReader.normalizeSpaces("  a   \t  b \n c\nd\n\fe  \rf\t \t    g h");
        assertEquals(expected, result);
    } 
    
    public void testGetEntities() throws ParseException {
        TagParser parser = TagParser.getInstance();
        parser.parse("!ENTITY % j2se '- Java 2 Standard Edition -'"); // this adds the entity to parameter entity database 
        parser.parse("!ENTITY % j2ee '- Java 2 Enterprise Edition -'"); // this adds the entity to parameter entity database 
        parser.parse("!ENTITY % apos '\"'"); 
        
        String testString = "%j2se;%j2ee;";
        String result = DTDReader.getEntities(testString);
        assertEquals(result, "- Java 2 Standard Edition -- Java 2 Enterprise Edition -");
        
        String testString2 = "%apos; %apos;";
        String result2 = DTDReader.getEntities(testString2);
        assertEquals(result2, "\" \"");
    }
    
}
