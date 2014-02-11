package argonavis.dtd;

/**
 * Represents a parsed DTD entity tag ------------ not done not done not done
 */
public class ConditionalInclusionTag implements Tag {

    private String contents;
    private boolean ignore;
    
    public ConditionalInclusionTag(String contents, boolean ignore) {
        this.contents = contents;
        this.ignore = ignore;
    }
    
    public String getContents() {
        return contents;
    }
    
    public String getAction() {
        return ignore ? "IGNORE" : "INCLUDE";
    }
    
    public String toString() {
        return "<![" + getAction() + "[" + contents + "]]>";
    }
    
    public boolean equals (Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof ConditionalInclusionTag)) return false;
        ConditionalInclusionTag other = (ConditionalInclusionTag)obj;
        return (contents.equals(other.contents) && 
                ignore == other.ignore);
    }
}
