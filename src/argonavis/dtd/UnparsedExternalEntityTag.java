package argonavis.dtd;

import argonavis.dtd.parsers.NotationTagParser;

public class UnparsedExternalEntityTag extends ExternalGeneralEntityTag {
    
    private String notationName;
    private String notationValue;
    
    public UnparsedExternalEntityTag(String name, String value, String notationName) {
        super(name, value);
        this.notationName = notationName;
    }
    
    public String getNotationName() {
        return notationName;
    }
    
    public String getNotationValue() throws ParseException {
        if (notationValue == null) {
            NotationTagParser parser = NotationTagParser.getInstance();
            try {
                notationValue = parser.resolveNotation(notationName);
            } catch (NotationNotFoundException e) {
                throw new ParseException("Unable to resolve notation " + notationName + " for unparsed external entity.");
            }
        }
        return notationValue;
    }
    
    public String toString() {
        return "<!ENTITY " + getName() + " SYSTEM \"" + getValue() + "\" NDATA "+ notationName + ">";
    }
    
    public boolean equals (Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof UnparsedExternalEntityTag)) return false;
        UnparsedExternalEntityTag other = (UnparsedExternalEntityTag)obj;
        return super.equals(other) &&
              (notationName.equals(other.notationName));
    }
}
