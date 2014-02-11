package argonavis.dtd.tagdata;

public class DefaultValueDeclaration {
    
    public static final DefaultValueDeclaration REQUIRED = new DefaultValueDeclaration() {
        public String toString() {
            return "#REQUIRED";
        }
    };
    
    public static final DefaultValueDeclaration IMPLIED = new DefaultValueDeclaration() {
        public String toString() {
            return "#IMPLIED";
        }
    };
    
    private boolean isFixed;
    private String defaultValue = "";
    
    private DefaultValueDeclaration() {}
    
    public DefaultValueDeclaration(String defaultValue) {
        this(defaultValue, false);
    }
    
    public DefaultValueDeclaration(String defaultValue, boolean isFixed) {
        this.isFixed = isFixed;
        this.defaultValue = defaultValue;
    }
    
    public String getDefaultValue() {
        return defaultValue;
    }
    
    public String toString() {
        String ifFixedString = "";
        if (isFixed) {
            ifFixedString = "#FIXED ";
        }
        return ifFixedString + "\"" + defaultValue + "\"";
    }
    
    public boolean equals(Object o) {
        return o.toString().equals(this.toString());
    }
    
}
