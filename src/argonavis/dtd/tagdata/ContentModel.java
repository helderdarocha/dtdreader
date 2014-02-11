package argonavis.dtd.tagdata;

/**
 */
public abstract class ContentModel extends ContentModelItem {

    private ContentModelItem[] contents;
    
    protected ContentModel(ContentModelItem[] contents) {
        this(contents, ONE);
    }
    
    protected ContentModel(ContentModelItem[] contents, String suffix) {
        super(suffix);
        this.contents = contents; 
    }
    
    public abstract char getSeparator();

    public ContentModelItem[] getContents() {
        return contents;
    }
    
    public String toString() {
        return "(" + printContentsAsString(this.getSeparator()) + ")" + this.getSuffix();
    }
    
    public boolean equals (Object obj) {
        // String representation of Content Model is unique
        return this.toString().equals(obj.toString());
    }
    
    public String printContentsAsString(char separator) {
        StringBuffer buffer = new StringBuffer(contents.length * 7);
        // estimated word + separator length
        for (int i = 0; i < contents.length; i++) {
            if (i != 0) {
                buffer.append(separator);
            }
            buffer.append(contents[i].toString());
        }
        return buffer.toString();
    }

}

