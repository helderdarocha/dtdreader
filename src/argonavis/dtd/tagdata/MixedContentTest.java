package argonavis.dtd.tagdata;

import junit.framework.*;
import argonavis.dtd.Element;

public class MixedContentTest extends TestCase {
    
    public MixedContentTest(java.lang.String testName) {
        super(testName);
    }
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    public static Test suite() {
        TestSuite suite = new TestSuite(MixedContentTest.class);
        return suite;
    }

    MixedContent mix; 
    String[] children = {"one", "two", "three", "four", "five", "six", "seven", "eight"};    
    ChildElement[] contents = new ChildElement[children.length];
    
    public void setUp() {
        for (int i = 0; i < children.length; i++) {
            contents[i] = new ChildElement(new Element(children[i]));
        }
        mix = new MixedContent(contents);
    }
    
    //public void setContents(ChildElement[] newContents)
    public void testSetContents() {
        String[] otherChildren = {"bellatrix", "betelgeuse", "alnitak", "saiph", "rigel", "mintaka"}; 
        ChildElement[] otherContents = new ChildElement[otherChildren.length];
        for (int i = 0; i < otherChildren.length; i++) {
            contents[i] = new ChildElement(new Element(otherChildren[i]));
        }
        mix.setContents(otherContents);
        assertEquals(otherContents, mix.getContents());
    }

    // public ChildElement[] getContents()
    public void getContents() {
        assertEquals(contents, mix.getContents());
    }

    // This method automatically tests printContentsAsString()
    public void testToString() {
        String expected = "(#PCDATA|one|two|three|four|five|six|seven|eight)*";
        String received = mix.toString();
        assertEquals(expected, received);
    }
    
    public void testEquals() {
        for (int i = 0; i < children.length; i++) {
            contents[i] = new ChildElement(new Element(children[i]));
        }
        MixedContent mix2 = new MixedContent(contents);
        assertEquals(mix2, mix);
    }

}

