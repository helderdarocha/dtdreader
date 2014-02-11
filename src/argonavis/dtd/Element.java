package argonavis.dtd;

import argonavis.dtd.tagdata.*;
import java.util.*;

/**
 * Represents a DTD element
 */
public class Element implements Comparable {
    
    private String name;
    private ElementContent contentModel;
    private Attribute[] attributes;
    
    public Element(String name) {
        this.name = name;
    }
    
    public Element(String name, ElementContent contentModel) {
        this.name = name;
        this.contentModel = contentModel;
    }

    public void setAttributes(Attribute[] attributes) {
        this.attributes = attributes;
    }
    
    public Attribute[] getAttributes() {
        return attributes;
    }
    
    public Element[] getChildren() {
        return getElements(contentModel);
    }
    
    public String getName() {
        return name;
    }
    
    public int hashCode() {
        int result = 17;
        result = 37*result + name.length();
        return result;
    }
    
    public boolean equals(Object obj) {
        if (obj instanceof Element) {
            Element e = (Element) obj;
            // In a DTD, the NAME identifies the element
            // (there may not be different elements defined with the same name)
            if (e.name.equals(this.name)) {
                return true;
            }
        }
        return false;
    }
    
    public int compareTo(Object obj) {
        return name.compareTo( ((Element)obj).name );
    }
    
    public String toString() {
        return name;
    }
    
    public static Element[] getElements(ElementContent contents) {
        List childList = new ArrayList();
        if (contents instanceof ContentModel) {
            ContentModel model = (ContentModel)contents;
            ElementContent[] items = model.getContents();
            for (int i = 0; i < items.length; i++) {
                Element[] elements = getElements(items[i]);
                for (int j = 0; j < elements.length; j++) {
                    childList.add(elements[j]);
                }
            }
        } else if (contents instanceof MixedContent) {
            MixedContent mixed = (MixedContent) contents;
            ChildElement[] items = mixed.getContents();
            for (int i = 0; i < items.length; i++) {
                Element[] elements = getElements(items[i]);
                for (int j = 0; j < elements.length; j++) {
                    childList.add(elements[j]);
                }
            }
        } else if (contents instanceof ChildElement) {
            childList.add(((ChildElement)contents).getElement());
        }
        return (Element[])childList.toArray(new Element[childList.size()]);
    }
}
