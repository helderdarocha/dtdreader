package argonavis.dtd.tagdata;

/**
 */
public class MixedContent implements ElementContent {

    private ChildElement[] contents;

    /**
     *  Creates mixed ( #PCDATA | a | b | c )* content model
     */
    public MixedContent(ChildElement[] contents) {
        this.contents = contents;
    }

    public void setContents(ChildElement[] newContents) {
        contents = newContents;
    }

    public ChildElement[] getContents() {
        return contents;
    }

    public String toString() {
        return "(#PCDATA|" + printContentsAsString() + ")*";
    }

    public String printContentsAsString() {
        StringBuffer buffer = new StringBuffer(contents.length * 7);
        for (int i = 0; i < contents.length; i++) {
            if (i != 0) {
                buffer.append("|");
            }
            buffer.append(contents[i].toString());
        }
        return buffer.toString();
    }
    
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if ( !(obj instanceof MixedContent) ) return false;
        MixedContent other = (MixedContent) obj;
        return this.toString().equals(other.toString());
    }

}

