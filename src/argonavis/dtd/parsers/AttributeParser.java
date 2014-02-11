package argonavis.dtd.parsers;

import argonavis.dtd.*;
import argonavis.dtd.tagdata.*;
import java.io.*;
import java.util.*;

/**
 * This class receives a set of attributes from an ATTLIST tag and
 * returns an array of Attribute objects
 */
public class AttributeParser {
    
    /** Valid separator for DTD Attribute Tag content models */
    public static final char separator = '|';
    
    private static AttributeParser instance = null;
    
    public static AttributeParser getInstance() {
        if (instance == null) {
            instance = new AttributeParser();
        }
        return instance;
    }
 
    private AttributeParser() {}
    
    /**
     * This method
     */
    public Attribute parse(String attributeString) throws ParseException {
        StringTokenizer tokens = new StringTokenizer(attributeString);
        String name = tokens.nextToken();
        String restOfString = tokens.nextToken("").trim();
        AttributeType type = null;
        if (restOfString.startsWith("NOTATION") || restOfString.startsWith("(")) {
            int openPar = restOfString.indexOf("(");
            int closePar = restOfString.indexOf(")");
            if (openPar < 0 || closePar < openPar || closePar < 0) {
                throw new ParseException("Missing or unbalanced parentheses!");
            }
            String enumeration = restOfString.substring(openPar, closePar+1);
            String[] contents = parseEnumeration(enumeration);
            if (openPar == 0) {
                type = new AttributeEnumeration(contents);
            } else {
                type = new AttributeNotationEnumeration(contents);
            }
            restOfString = restOfString.substring(closePar+1, restOfString.length()).trim();
        } else {
            tokens = new StringTokenizer(restOfString);
            String dataType = tokens.nextToken();
            if (dataType.equals("CDATA")) type = AttributeType.CDATA;
            if (dataType.equals("NMTOKEN")) type = AttributeType.NMTOKEN;
            if (dataType.equals("NMTOKENS")) type = AttributeType.NMTOKENS;
            if (dataType.equals("ENTITY")) type = AttributeType.ENTITY;
            if (dataType.equals("ENTITIES")) type = AttributeType.ENTITIES;
            if (dataType.equals("ID")) type = AttributeType.ID;
            if (dataType.equals("IDREF")) type = AttributeType.IDREF;
            if (dataType.equals("IDREFS")) type = AttributeType.IDREFS;
            
            if (type == null) {
                throw new ParseException("Unknown Data Type: " + dataType);
            }
            restOfString = tokens.nextToken("").trim();
        }
        
        DefaultValueDeclaration declaration = null;

        if (restOfString.equals("#REQUIRED")) {
            declaration = DefaultValueDeclaration.REQUIRED;
        } else if (restOfString.equals("#IMPLIED")) {
            declaration = DefaultValueDeclaration.IMPLIED;
        } else {
            String literal = null;
            boolean isFixed = false;
            if (restOfString.startsWith("#FIXED")) {
                literal = restOfString.substring("#FIXED".length(), restOfString.length()).trim();
                isFixed = false;
            } else {
                literal = restOfString.trim();
            }
            if ( (literal.startsWith("\"") && literal.endsWith("\"")) || 
                 (literal.startsWith("'")  && literal.endsWith("'")) )  {
                     
                declaration = new DefaultValueDeclaration(literal.substring(1, literal.length()-1), isFixed);
            } else {
                throw new ParseException ("Literal value must be enclosed in quotes or apostrophes: " + literal + ".");
            }
        }
        return new Attribute(name, type, declaration);
    }

    public String[] parseEnumeration(String string) throws ParseException {
        try {
            string = DTDReader.removeWhiteSpaces(string);
        } catch (IOException e) {
            throw new ParseException("Unable to parse attriute enumeration.");
        }
        if (!(string.startsWith("(") && string.endsWith(")"))) {
            throw new ParseException("Attribute Enumeration should be encolosed in parentheses.");
        }
        string = string.substring(1, string.length()-1);
        StringTokenizer tokens = new StringTokenizer(string, "|");
        List names = new ArrayList();
        while(tokens.hasMoreTokens()) {
            names.add(tokens.nextToken());
        }
        return (String[])names.toArray(new String[names.size()]);
    }
    

    
}
