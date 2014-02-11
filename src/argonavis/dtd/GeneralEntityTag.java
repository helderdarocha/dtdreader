package argonavis.dtd;

/**
 * A general entity tag. Used to define entities such as &name;
 */
public class GeneralEntityTag extends EntityTag {
    
    public GeneralEntityTag(String name, String value) {
        super(name, value);
    }
    
    public boolean equals (Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof GeneralEntityTag)) return false;
        return super.equals(obj);
    }
}
