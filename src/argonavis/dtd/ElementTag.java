package argonavis.dtd;

import argonavis.dtd.tagdata.*;
import java.util.*;

/**
 * Represents a parsed DTD element tag
 */
public class ElementTag implements Tag {
    
    private String name;
    private ElementContent contentModel;
    
    public ElementTag(String name, ElementContent contentModel) {
        this.name = name;
        this.contentModel = contentModel;
    }
    
    public ElementContent getContentModel() {
        return contentModel;
    }

    public String getName() {
        return name;
    }
    
    public String toString() {
        return "<!ELEMENT " + name + " " + contentModel + ">";
    }
    
    public boolean equals (Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof ElementTag)) return false;
        ElementTag other = (ElementTag)obj;
        return (name.equals(other.name) && contentModel.equals(other.contentModel));
    }
}
