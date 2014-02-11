package argonavis.dtd.tagdata;

public class AttributeType {
    
    public final static AttributeType CDATA     = new AttributeType("CDATA");
    public final static AttributeType NMTOKEN   = new AttributeType("NMTOKEN");
    public final static AttributeType NMTOKENS  = new AttributeType("NMTOKENS");
    public final static AttributeType ID        = new AttributeType("ID");
    public final static AttributeType IDREF     = new AttributeType("IDREF");
    public final static AttributeType IDREFS    = new AttributeType("IDREFS");
    public final static AttributeType ENTITY    = new AttributeType("ENTITY");
    public final static AttributeType ENTITIES  = new AttributeType("ENTITIES");
    
    private String typeName;
    
    private AttributeType(String typeName) {
       this.typeName = typeName;
    }
    
    /** This is for the Enumeration and Notation Enumeration types */
    protected AttributeType() {}
    
    public String toString() {
        return typeName;
    }
    
    public boolean equals(Object o) {
       return o.toString().equals(this.toString());
    }
}
