package argonavis.dtd;

/**
 * Represents a parsed DTD notation tag ------------ not done not done not done
 */
public class NotationTag implements Tag {
    
    public static final int SYSTEM_ID = 1;
    public static final int PUBLIC_ID = 2;
    
    private String name;
    private int source;
    private String value;
    
    public NotationTag(String name, String value) {
        this(name, value, SYSTEM_ID);
    }
    
    public NotationTag(String name, String value, int source) {
        this.name = name;
        this.value = value;
        if (source != SYSTEM_ID && source != PUBLIC_ID) {
            throw new IllegalArgumentException(source + " is not a valid argument for the NotationTag constructor.");
        }
        this.source = source;
    }
    
    public String getName() {
        return name;
    }
    
    public String getSourceIDType() {
        if (source == SYSTEM_ID) return "SYSTEM";
        else return "PUBLIC";
    }
    
    public String getValue() {
        return value;
    }
    
    public String toString() {
        return "<!NOTATION " + name + " " + getSourceIDType() + " " + "\"" + value + "\">";
    }
    
    public boolean equals (Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof NotationTag)) return false;
        NotationTag other = (NotationTag)obj;
        return (name.equals(other.name) && source == other.source && value.equals(other.value));
    }
}
