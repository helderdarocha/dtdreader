package argonavis.dtd.tagdata;

/**
 * Child Elements or Content Models
 */
public class ContentModelItem implements ElementContent { 

    public static final String ONE          = "";                 
    public static final String ZERO_OR_MORE = "*";
    public static final String ONE_OR_MORE  = "+";       
    public static final String ZERO_OR_ONE  = "?";   
    
    private String suffix;
    
    protected ContentModelItem(String suffix) {
        this.suffix = suffix;
    }
    
    public String getSuffix() {
        return suffix; 
    }   
    
    public String toString() {
        return suffix;
    }
    
    public boolean equals(Object o) {
        if (o == this) return true;
        return o.toString().equals(this.toString());
    }
}
