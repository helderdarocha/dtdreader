package argonavis.dtd;

/**
 * Represents a parsed DTD entity tag ------------ not done not done not done
 */
public class CommentTag implements Tag {
    
    private String contents;
    
    public CommentTag(String contents) {
        this.contents = contents;
    }
    
    public String getContents() {
        return contents;
    }
    
    public String toString() {
        return "<!-- " + contents + " -->";
    }
    
    public boolean equals (Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof CommentTag)) return false;
        CommentTag other = (CommentTag)obj;
        return contents.equals(other.contents);
    }
}
