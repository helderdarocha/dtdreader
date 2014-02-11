package argonavis.dtd.parsers;

import argonavis.dtd.*;
import java.util.*;

public class EntityTagParser {
    
    // debug flag
    public boolean debug = false;
    
    private static EntityTagParser instance = null;

    public static EntityTagParser getInstance() {
        if (instance == null) {
            instance = new EntityTagParser();
        }
        return instance;
    }
 
    private EntityTagParser() {}
    
    private HashMap parameterEntityTable = new HashMap();
    private HashMap generalEntityTable = new HashMap();

    public HashMap getParameterEntityTable() {
        return parameterEntityTable;
    }
    
    public HashMap getGeneralEntityTable() {
        return generalEntityTable;
    }
    
    public void addEntity(EntityTag tag) {
        if (tag instanceof ParameterEntityTag) {
            parameterEntityTable.put(tag.getName(), tag);
        } else {
            generalEntityTable.put(tag.getName(), tag);
        }
    }
    
    /**
     * 
     */
    public ParameterEntityTag resolveParameterEntity(String name) throws EntityNotFoundException {
        ParameterEntityTag tag = (ParameterEntityTag)parameterEntityTable.get(name);
        if (tag == null) {
            throw new EntityNotFoundException("No parameter entity matching '" + name + "' was found.");
        }
        return tag;
    }
    
    /**
    *  @return Returns an EntityTag object
     * @param tagContents Receives the content of an element tag which does NOT include the
     * string "&lt;!ENTITY" nor "&gt;" but all the text in between.
     */
    public EntityTag parse(String tagContents) throws ParseException {
        if ( tagContents.startsWith("<")  || tagContents.endsWith(">")  || tagContents.startsWith("!ENTITY") ) {
            throw new ParseException("Contents of pre-parsed DTD <!ENTITY> tag may not contain <, > or start with !ENTITY.");  
        }
        StringTokenizer tokens = new StringTokenizer(tagContents.trim(), " \n\t\r\f(");
        boolean parameterEntity = false;
        boolean external = false;
        boolean unparsed = false;
        
        String typeOrName = tokens.nextToken().trim();
        String entityName = ""; 
        
        if (typeOrName.equals("%")) {
            parameterEntity = true;
            entityName = tokens.nextToken().trim(); 
        } else {
            entityName = typeOrName;
        }
        
        
        
        
        if (debug) {
            System.out.println("typeOrName: |" + typeOrName + "|");
            System.out.println("entityName: |" + entityName + "|");
            System.out.println("parameter? |" + parameterEntity + "|");
        }
        
        
        
        
        // check if external entity
        String valueOrExternal = tokens.nextToken().trim(); // rest of tag
        
        
        
        
        if (debug) {
            System.out.println("valueOrExternal: |" + valueOrExternal + "|");
        }
        
        
        
        
        String value = "";
        if (valueOrExternal.equals("SYSTEM")) { // external entity
            external = true;
            value = tokens.nextToken("").trim(); // rest of tag
        } else {
            String moreData = "";
            if (tokens.hasMoreTokens()) {
                moreData = tokens.nextToken("");
            }
            value = (valueOrExternal + moreData).trim(); // recover token
        }
        
        
        if (debug) {
            System.out.println("external? |" + external + "|");
            System.out.println("value: |" + value + "|");
        }
        
        
        
        // check if unparsed external general entity
        String notationName = "";
        if (external) {
            int ndataPos = value.indexOf("NDATA");
            if (ndataPos != -1) {
                unparsed = true;
                notationName = value.substring(ndataPos + "NDATA".length(), value.length()).trim(); // last token
                value = value.substring(0, ndataPos).trim(); // first token
            }
        }
        
        
        if (debug) {
            System.out.println("unparsed? |" + unparsed + "|");
            System.out.println("value: |" + value + "|");
        }
        
        if (!((value.startsWith("\"") && value.endsWith("\"")) || 
              (value.startsWith("'")  && value.endsWith("'"))))  {
            throw new ParseException ("Entity value must be enclosed in quotation marks or apostrophes: " + value + ".");
        }
        value = value.substring(1, value.length()-1); // remove quotes or apostrophes
        
        EntityTag tag = null;
        if (parameterEntity) {
            if (external) {
                tag = new ExternalParameterEntityTag(entityName, value);
            } else {
                tag = new ParameterEntityTag(entityName, value);
            }
        } else { // general entity
            if (external) {
                if (unparsed) {
                    tag = new UnparsedExternalEntityTag(entityName, value, notationName);
                } else {
                    tag = new ExternalGeneralEntityTag(entityName, value);
                }
            } else {
                tag = new GeneralEntityTag(entityName, value);
            }
        }
        addEntity(tag);
        return tag;
    }
    
}
