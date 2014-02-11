package argonavis.dtd;

/**
 * Represents a parsed DTD entity tag
 */
public class ParameterEntityTag extends EntityTag {
    
    private String externalSubset;
    
    public ParameterEntityTag(String name, String value) {
        super(name, value);
    }
    
    public String toString() {
        return "<!ENTITY % " + getName() + " \"" + getValue() + "\" >";
    }

    public boolean equals (Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof ParameterEntityTag)) return false;
        return super.equals(obj);
    }
}
