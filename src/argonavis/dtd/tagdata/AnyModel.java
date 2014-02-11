package argonavis.dtd.tagdata;

public class AnyModel implements ElementContent { 
    
    public String toString() {
        return "ANY";
    }
    
    public boolean equals (Object obj) {
        // String representation of Content Model is unique
        return this.toString().equals(obj.toString());
    }

}
