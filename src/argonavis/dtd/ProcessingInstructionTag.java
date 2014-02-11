package argonavis.dtd;

/**
 * Represents a parsed DTD entity tag ------------ not done not done not done
 */
public class ProcessingInstructionTag implements Tag {
    
    private String contents;
    private String name;
    
    public ProcessingInstructionTag(String name, String contents) {
        this.contents = contents;
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public String getContents() {
        return contents;
    }
    
    public String toString() {
        return "<?" + name + " " + contents + " ?>";
    }
    
    public boolean equals (Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof ProcessingInstructionTag)) return false;
        ProcessingInstructionTag other = (ProcessingInstructionTag)obj;
        return contents.equals(other.contents) && name.equals(other.name);
    }
}
