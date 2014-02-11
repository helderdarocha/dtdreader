package argonavis.dtd;

/**
 * Represents a parsed DTD entity tag ------------ not done not done not done
 */
public class EntityTag implements Tag {
    
    private String name;
    private String value;
    
    protected EntityTag(String name, String value) {
        this.name = name;
        this.value = value;
    }
    
    public String getName() {
        return name;
    }
    
    public String getValue() {
        return value;
    }
    
    public String toString() {
        return "<!ENTITY " + name + " \"" + value + "\">";
    }
    
    public boolean equals (Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof EntityTag)) return false;
        EntityTag other = (EntityTag)obj;
        return name.equals(other.name) && 
                value.equals(other.value);
    }
}
