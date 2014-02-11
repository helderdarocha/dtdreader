package argonavis.dtd.tagdata;

import argonavis.dtd.parsers.NotationTagParser;
import argonavis.dtd.*;

public class AttributeNotationEnumeration extends AttributeEnumeration {

    public AttributeNotationEnumeration(String[] names) {
       super(names);
    }
    
    /**
     * This method returns the real (translated) enumeration values.
     */
    public String[] getResolvedNames() throws NotationNotFoundException {
        String[] unresolvedNames = super.getNames();
        String[] names = new String[unresolvedNames.length];
        // resolve names
        NotationTagParser parser = NotationTagParser.getInstance();
        
            for (int i = 0; i < names.length; i++) {
                String value = parser.resolveNotation(unresolvedNames[i]);
                names[i] = value;
            }
        return names;
    }
    
    /**
     * These names are not resolved.
     */
    public String toString() {
        return "NOTATION " + super.toString();
    }
    
    public boolean equals(Object o) {
        return o.toString().equals(this.toString());
    }
}
