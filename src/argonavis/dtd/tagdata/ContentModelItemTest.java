package argonavis.dtd.tagdata;

import junit.framework.*;

public class ContentModelItemTest extends TestCase { 

    public ContentModelItemTest(java.lang.String testName) {
        super(testName);
    }
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    public static Test suite() {
        TestSuite suite = new TestSuite(ContentModelItemTest.class);
        return suite;
    }
    
    private String[] strings, suffixes;
    ContentModelItem[] items;
    
    public void setUp() {
        strings  = new String[]{"", "*", "+", "?"};
        suffixes = new String[] {ContentModelItem.ONE, ContentModelItem.ZERO_OR_MORE, 
                                 ContentModelItem.ONE_OR_MORE, ContentModelItem.ZERO_OR_ONE};
        // This is a protected constructor - normal users wouldn't have so much access
        items = new ContentModelItem[strings.length];    
        for (int i = 0; i < items.length; i++) {
            items[i] = new ContentModelItem(suffixes[i]);
        }
    }
    
    public void testGetSuffix() {
        for (int i = 0; i < items.length; i++) {
            assertEquals(suffixes[i], items[i].getSuffix());
            assertEquals(strings[i], suffixes[i]);
        }
    }    
    
    public void testToString() {
        for (int i = 0; i < items.length; i++) {
            assertEquals(strings[i], items[i].toString());
        }
    }    
    
    public void testEquals() {
        for (int i = 0; i < items.length; i++) {
            assertEquals(items[i], suffixes[i]);
            assertEquals(items[i], strings[i]);
        }
    }    
}
