package argonavis.dtd;

import argonavis.dtd.parsers.NotationTagParser;
import argonavis.dtd.tagdata.*;

public class Attribute implements Comparable {
    
    private String  name;
    private AttributeType type;
    private DefaultValueDeclaration declaration;
    
    public Attribute(String name, AttributeType type, DefaultValueDeclaration declaration) {
        this.name = name;
        this.type = type;
        this.declaration = declaration;
    }
    
    public String getName() {
        return name;
    }
    
    public AttributeType getAttributeType() {
        return type;
    }
    
    public DefaultValueDeclaration getDefaultDeclaration() {
        return declaration;
    }
    
    public String toString() {
        return name + " " + type + " " + declaration;
    }
    
    public int hashCode() {
        int result = 17;
        result = 37 * result + name.length();
        return result;
    }
    
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if ( !(obj instanceof Attribute) ) return false;
        Attribute att = (Attribute) obj;
        return att.name.equals(this.name);
    }
    
    /**
     * For sorting by attribute name
     */
    public int compareTo(Object obj) {
        return name.compareTo( ((Attribute)obj).name );
    }

}
