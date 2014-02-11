package argonavis.dtd.parsers;

import argonavis.dtd.*;
import java.util.*;

import junit.framework.*;

/**
 * Converts a DTD isto a set of tag arrays.
 * Creates a set of Element arrays from the results.
 */
public class EntityTagParserTest extends TestCase {
    
    public EntityTagParserTest(java.lang.String testName) {
        super(testName);
    }
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    public static Test suite() {
        TestSuite suite = new TestSuite(EntityTagParserTest.class);
        return suite;
    }
    
    EntityTagParser parser; 
    String[] strings, names;
    EntityTag[] tags;

    public void setUp() {
        parser = EntityTagParser.getInstance();
        // when adding new values, add to the three arrays: names, strings and tags
        names = new String[] {"boolean", "icon", "icon", "data", "copyright", "subdata", "home"};
        // Only for parsing (has apostrophes as value delimiters)
        strings = new String[] {
            "% boolean \"true|false\"",
            "% icon \"NOTATION (gif | jpg | png)\"",                                // Parameter Entity
            "icon SYSTEM \"/images/logo32.gif\" NDATA gif",                         // Unparsed External General Entity
            "% data \"title, author, description\" ",
            "copyright '&lt;p&gt;&copy; 2002 Unlimited \"U\" &amp; &apos;I&apos; Inc.'",  // General Entity 
            "% subdata SYSTEM \"subdata.dtd\"",                                     // External Parameter Entity 
            "home SYSTEM \"index.html\""                                            // External General Entity
        };  
        
        tags = new EntityTag[] {
            new ParameterEntityTag("boolean", "true|false"),
            new ParameterEntityTag("icon", "NOTATION (gif | jpg | png)"),        // Parameter Entity
            new UnparsedExternalEntityTag("icon", "/images/logo32.gif", "gif"),   // Unparsed External General Entity
            new ParameterEntityTag("data", "title, author, description"),
            new GeneralEntityTag("copyright", "&lt;p&gt;&copy; 2002 Unlimited \"U\" &amp; &apos;I&apos; Inc."),  // General Entity 
            new ExternalParameterEntityTag("subdata", "subdata.dtd"),            // External Parameter Entity 
            new ExternalGeneralEntityTag("home", "index.html")                   // External General Entity
        };
    }
    
    public void testGetParameterEntityTable() {
        assertNotNull(parser.getParameterEntityTable());
    }
    
    public void testGetGeneralEntityTable() {
        assertNotNull(parser.getGeneralEntityTable());
    }
    
    public void testAddEntity() {
        for (int i = 0; i < tags.length; i++) {
            parser.addEntity(tags[i]);
        }
        assertStorage();
    }

    public void testResolveParameterEntity() throws EntityNotFoundException {
        testAddEntity(); // fill with some tags
        for (int i = 0; i < tags.length; i++) {
            if (tags[i] instanceof ParameterEntityTag) {
                ParameterEntityTag tag = parser.resolveParameterEntity(names[i]);
                assertEquals(tag, tags[i]);
            }
        }
    }

    // EntityTag parse(String tagContents)
    public void testParse() throws ParseException {
        for (int i = 0; i < tags.length; i++) {
            EntityTag tag = parser.parse(strings[i]);
            assertEquals(tag, tags[i]);
        }
        // Check and see if additions were made
        assertStorage();
    }
    
    public void assertStorage() {
        HashMap peMap = parser.getParameterEntityTable();
        HashMap geMap = parser.getGeneralEntityTable();
        for (int i = 0; i < tags.length; i++) {
            EntityTag tag = null;
            if (tags[i] instanceof ParameterEntityTag) {
                tag = (EntityTag)peMap.get(names[i]);
            } else {
                tag = (EntityTag)geMap.get(names[i]);
            }
            assertEquals(tag, tags[i]);
        }
    }
    
}
