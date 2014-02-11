package argonavis.dtd.tagdata;

public class EmptyModel implements ElementContent { 

    public String toString() {
        return "EMPTY";
    }
    
    public boolean equals (Object obj) {
        // String representation of Content Model is unique
        return this.toString().equals(obj.toString());
    }

}
