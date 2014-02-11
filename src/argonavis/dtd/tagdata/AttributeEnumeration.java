package argonavis.dtd.tagdata;

public class AttributeEnumeration extends AttributeType {
    
    String[] names;

    public AttributeEnumeration(String[] names) {
       this.names = names;
    }
    
    public String[] getNames() {
        return names;
    }
    
    public String toString() {
        StringBuffer namesBuffer = new StringBuffer();
        for (int i = 0; i < names.length; i++) {
            if (i != 0) {
                namesBuffer.append("|");
            }
            namesBuffer.append(names[i]);
        }
        return "(" + namesBuffer + ")";
    }
    
    public boolean equals(Object o) {
        return o.toString().equals(this.toString());
    }
}
