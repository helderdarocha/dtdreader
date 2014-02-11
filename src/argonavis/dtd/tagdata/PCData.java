package argonavis.dtd.tagdata;

public class PCData implements ElementContent { 
    
    public String toString() {
        return "( #PCDATA )";
    }
    
    public boolean equals (Object obj) {
        // String representation of Content Model is unique
        return this.toString().equals(obj.toString());
    }
}
