package argonavis.dtd.parsers;

import argonavis.dtd.*;
import argonavis.dtd.tagdata.*;
import java.io.*;
import java.util.*;

public class ContentModelParser {
    
    /** Valid separators for DTD Element Tag content models */
    public static final char[] separators = {',', '|'};
    
    private static ContentModelParser instance = null;
    
    public static ContentModelParser getInstance() {
        if (instance == null) {
            instance = new ContentModelParser();
        }
        return instance;
    }
 
    private ContentModelParser() {}
    
    public ElementContent parse(String modelString) throws ParseException {
        
        if (modelString.equals("EMPTY")) {
            return new EmptyModel();
        } else if (modelString.equals("ANY")) {
            return new AnyModel();
        } else {
            try {
                modelString = removeWhiteSpaces(modelString);
                
            } catch (IOException e) {
                throw new ParseException("I/O Exception when parsing content model for element tag");
            }
            return parseExpression(modelString);
        }
    }
    public ContentModelItem parseExpression(String string) throws ParseException {
        
        char separator = '-';                               // expression separator ',' or '|'
        String suffix = null;                               // repetition rule
        StringBuffer elementBuffer = new StringBuffer();          // element names
        StringBuffer subExpressionBuffer = new StringBuffer();    // a deeper level (reached by recursion)
        List list = new ArrayList();
        
        // Expression must be in the format: (xxxxxx) or (xxxxxx)* or (xxxxxx)+ or (xxxxxx)?
        boolean validStart = string.startsWith("(");
        boolean hasSuffix  = string.endsWith(")*") || string.endsWith(")+") || string.endsWith(")?");
        boolean noSuffix   = string.endsWith(")");
        boolean validEnd = hasSuffix || noSuffix;
        if ( !(validStart && validEnd) ) {
            throw new ParseException ("Expression not enclosed in parentheses: " + string);
        }
        if (noSuffix) {
            suffix = ContentModelItem.ONE;  // no symbol
            string = string.substring(1, string.length()-1); // remove parentheses before parsing
        } else { // hasSuffix
            suffix = getSuffix(string.charAt(string.length()-1));
            string = string.substring(1, string.length()-2); // remove parentheses before parsing
        }
        
        // any parentheses, from now on, belong to a deeper level.
        int openCount = 0;
        int len = string.length();
        for (int i = 0; i < len; i++) {
            char c = string.charAt(i);
            if (c == '(') {
                openCount++;
            } else if (c == ')') {
                openCount--;
            } 
            if (openCount == 0 && c != ')') { // first level elements only
                if (c == '|' || c == ',') {
                    if (elementBuffer.length() != 0) {
                        list.add(getChildElement(elementBuffer.toString()));
                        elementBuffer.setLength(0);
                    }
                    if (separator == '-') { // if no separator was defined, define c as separator for this set
                        separator = c;
                    }
                    if (separator != c) { // different separators in same set is illegal
                        throw new ParseException("Malformed element tag content model. Different separators in same set: found: '" + separator + "' when expecting '" + c + "'.");
                    }
                } else { 
                    elementBuffer.append(c);  
                }
            } else { // second level expression for parsing via recursion
                subExpressionBuffer.append(c);
                if (openCount == 0) { // this is the final ")"
                    char nextChar = '.';
                    if (i != len -1) {
                        nextChar = string.charAt(i+1);
                    }
                    if (nextChar == '*' || nextChar == '?' || nextChar == '+') {
                        // do something special
                        subExpressionBuffer.append(nextChar);
                        i++; // skipping the char
                    } // no suffix - add now to the list
                    list.add(parseExpression(subExpressionBuffer.toString()));
                    subExpressionBuffer.setLength(0);

                }
            }
        }

        if (elementBuffer.length() > 0) {
            list.add(getChildElement(elementBuffer.toString()));
        }

        ContentModelItem[] contents = (ContentModelItem[])list.toArray(new ContentModelItem[list.size()]);
        if (separator == ',') {
            return new SequenceContentModel(contents, suffix);
        } else if (separator == '|' || contents.length == 1) {
            return new ChoiceContentModel(contents, suffix);
        }
        throw new ParseException("Illegal separator: '" + separator + "'.");
    }
    
    boolean nextCharIsSuffix(String string, int i) {
        int ultimoChar = string.length()-1;
        if (i == ultimoChar) return false;
        int prox = i+1;
        if (string.charAt(prox) == '*' || string.charAt(prox) == '+' || string.charAt(prox) == '?') {
            return true;
        }
        return false;
    }
    
    /**
     * To deprecate - useful method - save in utilities!
     */
    String[] tokenizeParentheses(String str) throws ParseException {
        int openCount = 0;
        char separator;
        StringBuffer element = new StringBuffer();
        List list = new ArrayList();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '(') {
                openCount++;
            } else if (c == ')') {
                openCount--;
            }
            if (openCount == 0) {
                if (c == '|' || c == ',') {
                    if (element.length() != 0) {
                        list.add(element.toString());
                        element.setLength(0);
                    } else {
                        throw new ParseException("Misplaced separator character: '" + c + "'.");
                    }
                    separator = c;
                } else { 
                    element.append(c);  
                }
            } else { // we are in second, third, ... level.
                element.append(c);
            }
        }
        list.add(element.toString());
        return (String[])list.toArray(new String[list.size()]);
    }
    
    /**
     * Returns a ChildElement from a string which contains the name of the
     * element and may contain a repetition rule suffix
     */
    ChildElement getChildElement(String string) {
        String suffix = getSuffix(string.charAt(string.length()-1));
        if (!suffix.equals("")) { // shorten main string if necessary
            string = string.substring(0, string.length() - 1);
        }   
        return new ChildElement(new Element(string.toString()), suffix);
    }
    
    /**
     * Returns a constant from a suffix
     */
    public static String getSuffix(char suffixChar) {
        if (suffixChar == '*') return ContentModelItem.ZERO_OR_MORE;
        if (suffixChar == '+') return ContentModelItem.ONE_OR_MORE;
        if (suffixChar == '?') return ContentModelItem.ZERO_OR_ONE;
        return "";
    }
    /**
     * This should go to utilities section 
     */
    public static String removeWhiteSpaces(String text) throws IOException {
        StringReader reader = new StringReader(text);
        StringBuffer buffer = new StringBuffer(text.length());
        int c;
        while( (c = reader.read()) != -1) {
            if (c == ' ' || c == '\n' || c == '\r' || c == '\f' || c == '\t') {
                ;
            } else {
                buffer.append((char)c);
            }
        }
        return buffer.toString();
    }
    
}
