package argonavis.dtd;

import java.io.*;

public class ExternalParameterEntityTag extends ParameterEntityTag {
    
    private String externalSubset;
    
    public ExternalParameterEntityTag(String name, String locator) {
        super(name, locator);
    }
    
    public String toString() {
        return "<!ENTITY % " + getName() + " SYSTEM \"" + getValue() + "\">";
    }
    
    /**
     * The real value for an external parameter entity is its contents
     */
    public String getContents() throws IOException {
        if (externalSubset == null) {
            //externalSubset = DTDLoader.load(getValue());
        }
        return "<!-- This feature is not yet supported -->";
        //return externalSubset;
    }
    
    /**
     * Same references, same entities
     */
    
    public boolean equals (Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof ExternalParameterEntityTag)) return false;
        return super.equals(obj);
    }
}
