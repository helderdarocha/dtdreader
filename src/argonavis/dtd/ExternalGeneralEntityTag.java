package argonavis.dtd;

public class ExternalGeneralEntityTag extends GeneralEntityTag {
    
    public ExternalGeneralEntityTag(String name, String value) {
        super(name, value);
    }
    
    public String toString() {
        return "<!ENTITY " + getName() + " SYSTEM \"" + getValue() + "\">";
    }
    
    public boolean equals (Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof ExternalGeneralEntityTag)) return false;
        ExternalGeneralEntityTag other = (ExternalGeneralEntityTag)obj;
        return super.equals(other);
    }
}
