package argonavis.dtd.parsers;

import argonavis.dtd.*;
import argonavis.dtd.tagdata.*;
import java.util.*;

import junit.framework.*;

public class TagParserTest extends TestCase {
    
    public TagParserTest(java.lang.String testName) {
        super(testName);
    }
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    public static Test suite() {
        TestSuite suite = new TestSuite(TagParserTest.class);
        return suite;
    }
    
    TagParser parser;
    Tag[] tags;
    String[] strings;

    public void setUp() throws ParseException {
        parser = TagParser.getInstance();   
        
        SortedSet formSetOne = new TreeSet();
        formSetOne.add(new Attribute("action", AttributeType.CDATA, DefaultValueDeclaration.REQUIRED));
        formSetOne.add(new Attribute("method", new AttributeEnumeration(new String[]{"GET","POST"}), 
                                                       new DefaultValueDeclaration("GET"))  );
        formSetOne.add(new Attribute("target", AttributeType.NMTOKEN, new DefaultValueDeclaration("_self")));

        // add notations so we can add one more attribute that depends on notations
        String[] notations = {"simple", "upload"};
        String[] notationValues = {"text/x-www-form-urlencoded", "text/multipart-form-data"};
        
        NotationTagParser notationParser = NotationTagParser.getInstance();
        for (int i = 0; i < notations.length; i++) {
            notationParser.addNotation(new NotationTag(notations[i], notationValues[i]));
        }
        SortedSet formSetTwo = new TreeSet();
        formSetTwo.add(new Attribute("encoding", new AttributeNotationEnumeration(new String[]{"simple", "upload"}), 
                                                         DefaultValueDeclaration.IMPLIED) );
                                                         
        // Extra tags - added Feb 6, 2002
        SortedSet set2 = new TreeSet();
        set2.add(new Attribute("id", AttributeType.ID, DefaultValueDeclaration.IMPLIED));
        set2.add(new Attribute("src", AttributeType.CDATA, DefaultValueDeclaration.REQUIRED));
        set2.add(new Attribute("type", new AttributeEnumeration(new String[]{"gif","jpg","png"}), new DefaultValueDeclaration("png")));
        // end extra tests
                                                         
        // Now empty notation table so entries dont exist before parsing (junit wont do it
        // because we use a singleton!)
        notationParser.getNotationTable().clear();

        tags = new Tag[] {
            new ParameterEntityTag("boolean", "true|false"),
            new ElementTag("form", ContentModelParser.getInstance().parse("(input|button|select|textarea)*")),
            new AttlistTag("form", formSetOne),
            new NotationTag("simple", "text/x-www-form-urlencoded"),
            new NotationTag("upload", "text/multipart-form-data"),
            new UnparsedExternalEntityTag("formdata", "/formdata.txt", "upload"),
            new AttlistTag("form", formSetTwo),
            new AttlistTag("image", set2)   // Extra tag - added Feb 6, 2002
        };
        
        strings = new String[] {
            "!ENTITY % boolean \"true|false\"",
            "!ELEMENT form (input|button|select|textarea)*",
            "!ATTLIST form action CDATA #REQUIRED method (GET|POST) \"GET\" target NMTOKEN \"_self\"",
            "!NOTATION simple SYSTEM \"text/x-www-form-urlencoded\"",
            "!NOTATION upload SYSTEM \"text/multipart-form-data\"",
            "!ENTITY formdata SYSTEM \"/formdata.txt\" NDATA upload",
            "!ATTLIST form encoding NOTATION (simple|upload) #IMPLIED",
            "!ATTLIST image id ID #IMPLIED\n" +
            "               src CDATA #REQUIRED\n" +
            "               type (gif | jpg | png) \"png\"" // Extra tag - added Feb 6, 2002
        };
    }

    public void testParse() throws ParseException {
        for (int i = 0; i < tags.length; i++) {
            Tag tag = parser.parse(strings[i]);
            assertEquals(tag, tags[i]);
        }
    }
    
}
