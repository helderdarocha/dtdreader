package argonavis.dtd;

import java.util.SortedSet;

/**
 * Represents a parsed DTD attribute tag ------------ not done not done not done
 */
public class AttlistTag implements Tag {
    
    private String elementName;
    private SortedSet attributeSet;
    
    public AttlistTag(String elementName, SortedSet attributeSet) {
        this.elementName = elementName;
        this.attributeSet = attributeSet;
    }
    
    public SortedSet getAttributes() {
        return attributeSet;
    }
    
    public String getElementName() {
        return elementName;
    }
    
    public String toString() {
        return "<!ATTLIST " + elementName + " " + printAttributeList() + ">";
    }
    
    public String printAttributeList() {
        int tabLength = ("<!ATTLIST " + elementName + " ").length();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < tabLength; i++) {
            buffer.append(" ");
        }
        String tabs = buffer.toString();
        
        buffer = new StringBuffer();
        Attribute[] array = (Attribute[])attributeSet.toArray(new Attribute[attributeSet.size()]);
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                buffer.append("\n" + tabs);
            }
            buffer.append(array[i].toString());
        }
        return buffer.toString();
    }
    
    /**
     * Incomplete!
     */
    public boolean equals (Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof AttlistTag)) return false;
        AttlistTag other = (AttlistTag)obj;
        return elementName.equals(other.elementName) && attributeSet.equals(other.attributeSet);
    }
}
