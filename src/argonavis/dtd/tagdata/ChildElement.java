package argonavis.dtd.tagdata;

import argonavis.dtd.Element;

public class ChildElement extends ContentModelItem { 
    
    private Element element;
    
    public ChildElement(Element element) {
        this(element, ONE);
    }
    
    public ChildElement(Element element, String suffix) {
        super(suffix);
        this.element = element;
    }
    
    public Element getElement() {
        return element; 
    }    
    
    public String toString() {
        return element.getName() + getSuffix();
    }
    
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if ( !(obj instanceof ChildElement)) return false;
        ChildElement other = (ChildElement)obj;
        return (other.element.equals(element) && other.getSuffix().equals(getSuffix()));
    }

}
