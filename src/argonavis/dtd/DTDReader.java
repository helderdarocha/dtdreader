package argonavis.dtd;

import java.io.*;
import java.util.*;

import argonavis.dtd.parsers.*;
import argonavis.dtd.tagdata.*;

/**
 * Converts a DTD isto a set of tag arrays.
 * Creates a set of Element arrays from the results.
 */
public class DTDReader {
    
    private ElementTag[] elementTags;
    private AttlistTag[] attlistTags;
    private EntityTag[] entityTags; // all entity tags, including parameter entity tags
    private ParameterEntityTag[] parameterEntityTags;
    private NotationTag[] notationTags;
    private CommentTag[] commentTags;
    
    private Tag[] allTags;
    
    private Element[] elements; 
    
    private String dtdString;

    public DTDReader() {
        //this.dtdString = dtdString;
    }
    
    /**
     * Returns all Elements
     */
    public Element[] getElements() {
        return null;
    }
    
    public Tag[] getTags() {
        return allTags;
    }
    static int a = 0;
    public static DTDComponent[] parse(String dtd) throws ParseException {

        boolean openAngle = false; // angle bracket that has not been closed --> true
        boolean openQuot  = false; // quote that has not been closed --> true
        boolean openApos  = false; // apostrophe that has not been closed --> true
        boolean isComment = false;
        boolean isPI = false;
        
        boolean quoted = openQuot || openApos;
        boolean commented = isPI || isComment;
        boolean ignore = quoted || commented;
        
        StringTokenizer tokens = new StringTokenizer(dtd, "<>\"'", true);
        StringBuffer unparsed = new StringBuffer();
        String token = "";
        String previous = "";
        List list = new ArrayList();
        
        while (tokens.hasMoreTokens()) {
            
            quoted = openQuot || openApos;
            commented = isPI || isComment;
            ignore = quoted || commented;
            
            previous = token;
            token = tokens.nextToken();
            if (token.startsWith("%") && previous.equals(">")) { // one or more %parameterEntity; outside tag (dtd subset)
                
                
                //System.out.println("<<<<< external %nnn; >>>>>  " + token);

                // CHECK THIS CAREFULLY!!!!
                
                
                if (token.endsWith(";")) { // DTD subset (may contain many tags and other nested subsets!
                    String dtdSubset = DTDReader.getEntities(token);
                    DTDComponent[] comps = DTDReader.parse(dtdSubset); // RECURSIVE CALL
                    for (int i = 0; i < comps.length; i++) {
                        
                        System.out.println("\n\n(((" + ++a + "))) Added: " + comps[i]);
                        
                        list.add(comps[i]); // adds each parsed component to list
                    }
                } else {
                    throw new ParseException("Malformed entity: " + token);
                }
            } else if (!ignore && !openAngle) {    // outside a tag
                
                
                
                //System.out.println("<<<<< not inside a tag, not inside C, PI, Q, A >>>>>    " + token);
                
                
                
                if (token.equals("<")) {    // it is a tag
                    
                    
                    
                    //System.out.println("<<<<< Start '<' >>>>>   " + token);
                    
                    
                    
                    openAngle = true;
                    unparsed.append(token);
                }
            } else if (openAngle && !ignore) { // inside tag
                
                    
                    
                    //System.out.println("<<<<< Inside tag, not inside A, Q, C or I >>>>> " + token);
                    
                    
                    
                if (previous.equals("<")) { // start of tag
                    if (token.startsWith("!--")) {
                        
                    
                    
                    //System.out.println("<<<<< Start '<!--' >>>>>    " + token);
                    
                    
                    
                        isComment = true;
                        unparsed.append(token);
                    } else if (token.startsWith("?")) {
                        
                    
                    
                    //System.out.println("<<<<< Start '<?' >>>>>  " + token);
                    
                    
                    
                        isPI = true;
                        unparsed.append(token);
                    } else { // is normal tag
                        
                        //System.out.print(processTag(token));
                        
                        unparsed.append(processTag(token));
                    }
                } else { // somewhere else in tag but not within quotes, apostrophes or comments or PIs
                    
                    
                    
                    //System.out.println("<<<<< Inside tag, second char or more. Not in A or Q >>>>>  " + token);
                    
                    
                    
                    if (token.equals("\"")) { // quote found
                        
                    
                    
                    //System.out.println("<<<<< Start Quote >>>>> " + token);
                    
                    
                    
                        openQuot = true;
                        unparsed.append(token);
                    } else if (token.equals("'")) { // apostrophe found
                        
                    
                    
                    //System.out.println("<<<<< Start Apostrophe >>>>>    " + token);
                    
                    
                    
                        openApos = true;
                        unparsed.append(token);
                    } else if (token.equals(">")) { // tag is done ----- ADDS TAG --- PARSES TAG
                        
                    
                    
                    //System.out.println("<<<<< This token is '>' - tag is done >>>>> " + token);
                    
                    
                    
                        openAngle = false;
                        unparsed.append(token);
                        list.add(parseComponent(unparsed.toString())); // PARSE TAG
                        unparsed.setLength(0);
                    } else {    // other tag content
                        
                        
                        //System.out.print(processTag(token));
                        
                        
                        unparsed.append(processTag(token));
                    }
                }
            } else if (ignore) { // inside comments, pis
                                        
                    
                    
                    //System.out.println("<<<<< We are in C, A, I or Q >>>>>  " + token);
                    
                    
                    
                if (commented) { // ignore everything except ">", check previous
                                            
                    
                    
                    //System.out.println("<<<<< We are in C or I >>>>>    " + token);
                    
                    
                    
                    if (token.equals(">")) {
                        if (previous.endsWith("?")) { // is PI ----- ADDS PI
                            
                                                    
                    
                    
                    //System.out.println("<<<<< PI is done >>>>>  " + token);
                    
                    
                    
                            
                            isPI = false;
                            unparsed.append(token);
                            list.add(parseComponent(unparsed.toString())); // adds PI
                            unparsed.setLength(0);
                        } else if (previous.endsWith("--")) { // is comment ------- ADDS COMMENT
                            
                                                    
                    
                    
                    //System.out.println("<<<<< Comment is done >>>>> " + token);
                    
                    
                    
                            isComment = false;
                            unparsed.append(token);
                            list.add(parseComponent(unparsed.toString())); // adds comment
                            unparsed.setLength(0);
                        } else { // other ">" inside a comment or PI
                            
                                                    
                    
                    
                    //System.out.println("<<<<< This token is '>' - ignore because its in comment or PI >>>>> " + token);
                    
                    
                    
                            
                            unparsed.append(token);
                        }
                    } else { // other comment or PI data
                        
                        
                        //System.out.print(processTag(token));
                        
                        
                        unparsed.append(token); // don't substitute entities in here
                    }
                } else if (openQuot) { // inside apos or quot but not in comment (in tag)
                                            
                    
                    
                    //System.out.println("<<<<< We are in Q >>>>> " + token);
                    
                    
                    
                    if (token.equals("\"")) { // this is the end    ------- ADDS quoted string
                        
                                                
                    
                    
                    //System.out.println("<<<<< End of quoted string >>>>>    " + token);
                    
                    
                    
                        
                        openQuot = false;
                        unparsed.append(token);
                    } else {
                        
                        
                        
                        //System.out.print(processTag(token));
                        
                        
                        unparsed.append(processTag(token));
                    }
                } else if (openApos) {
                                            
                    
                    
                    //System.out.println("<<<<< We are in A >>>>> " + token);
                    
                    
                    
                    if (token.equals("'")) { // this is the end    ------- ADDS apos string
                                                
                    
                    
                    //System.out.println("<<<<< End of apostrophed string >>>>>   " + token);
                    
                    
                    
                        openApos = false;
                        unparsed.append(token);
                    } else {
                        
                        
                        
                        unparsed.append(processTag(token));
                    }
                }
                                        
                    
                    
                    //System.out.println("<<<<< We are in limbo >>>>> " + token);
                    
                    
                    
            } else {
                throw new ParseException("Irregular string: " + token);
            }
                                    
                    
                    
                    //System.out.println("<<<<< We are in heaven >>>>>    " + token);
                    
                    
                    

        } // while - has More Tokens
        
        return (DTDComponent[])list.toArray(new DTDComponent[list.size()]);
    } // method
    
    public static String processTag(String token) throws ParseException {
        // "!ENTITY % red %apos;rouge%apos;"
        System.out.println("Tag before processing: " + token);
        
        int startEntity = token.indexOf("%");
        int endEntity = -1;
        StringBuffer buffer = new StringBuffer();
        char nextChar = (char)0;
        if (startEntity < token.length()-1) {
            nextChar = token.charAt(startEntity+1);
        }
        
        
        System.out.println("NEXT CHAR: " + nextChar);
        
        boolean space = (nextChar == ' ' || nextChar == '\n' || nextChar == '\r' || nextChar == '\f' || nextChar == '\t');
        
        System.out.println("startEntity: " + startEntity);
        System.out.println("space: " + space);
        
        if (startEntity != -1 && !space) { // has entity for substitution
            endEntity = token.indexOf(";", startEntity);
            if (endEntity == -1) {
                throw new ParseException("Malformed parameter entity in string: " + token + ".");
            }
            String entityName = token.substring(startEntity+1, endEntity); // does not include % and ;
            try {
                System.out.println("Entity name: " + entityName);
                String entityValue = resolveEntity(entityName); 
                buffer.append(token.substring(0, startEntity)); // preserve what exists before
                System.out.println("Entity value: " + entityValue);
                buffer.append(processTag(entityValue)); // converts nested entities
            } catch (EntityNotFoundException e) {
                throw new ParseException("Unable to resolve entity " + entityName + ".");
            }
            if (token.length() > endEntity + 1) {
                buffer.append(processTag(token.substring(endEntity+1, token.length())));
            }
        } else if (startEntity != -1 && space) { // look for other substitutions after '% '
            buffer.append(token.substring(0, 2));
            buffer.append(processTag(token.substring(2, token.length())));
        } else { 
            buffer.append(token);
        }
        
        System.out.println("Tag after processing: " + buffer.toString());
        
        return buffer.toString();
    }
    
    static int counter = 0;
    public static DTDComponent parseComponent(String dtdCompStr) throws ParseException {
        
        
        
        System.out.println(++counter + "####### Parsed DTD Component: [" + dtdCompStr + "]");
        
        
        dtdCompStr = dtdCompStr.trim();  //  remove extra spaces
        
        String contents = dtdCompStr.substring(1, dtdCompStr.length() - 1); // remove "<" and ">"
        DTDComponent component = null;
        if (contents.startsWith("!--")) {
            component = new CommentTag(contents.substring(3, contents.length() - 2));
        } else if (contents.startsWith("?")) {
            contents = contents.substring(1, contents.length()-1);
            StringTokenizer piTokens = new StringTokenizer(contents);
            String piName = piTokens.nextToken();
            String piContents = piTokens.nextToken("").trim();
            component = new ProcessingInstructionTag(piName, piContents);
        } else {
            component = TagParser.getInstance().parse(contents);
        }
        return component;
    }

    /**
     * More than one entity may be merged - separate by ";"
     * Get rid of this - make one for one, one, one entity!!!!
     */
    public static String getEntities(String entityList) throws ParseException {
        // split string into one or more entities
        StringTokenizer tokens = new StringTokenizer(entityList, ";");
        StringBuffer entitiesBuffer = new StringBuffer();
        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken();
            int percent = token.indexOf("%");
            if (percent == -1) {
                throw new ParseException("Malformed entity substitution string: " + token);
            }
            String text = "";
            String entityName = token.substring(percent+1, token.length());
            if (percent != 0) { // there is text before the entity string
                entitiesBuffer.append(token.substring(0, percent));
            }
            // resolve each entity
            try {
                entitiesBuffer.append(resolveEntity(entityName));
            } catch (EntityNotFoundException e) {
                throw new ParseException("Entity " + entityName + " not defined in DTD.");   
            }
        }
        return entitiesBuffer.toString();
    }
    
    /**
     * This method processes DTD subsets such as those added with &lt;[INCLUDE[ ... ]]&gt; or 
     * via parameter entities (%name; where the entity includes a whole file
     */
    public static String processDTDSubset(String subset) {
        return "Not yet supported";
    }
    
    public static String resolveEntity(String entityName) throws EntityNotFoundException, ParseException {
        EntityTagParser parser = EntityTagParser.getInstance();
        ParameterEntityTag entity = parser.resolveParameterEntity(entityName);
        String value = entity.getValue(); 
        String contents = value; // for common parameter entities
        if (entity instanceof ExternalParameterEntityTag) {
            try {
                contents = ((ExternalParameterEntityTag)entity).getContents();
            } catch (IOException e) {
                throw new ParseException("Unable to resolve external entity: " + entityName + ".");   
            }
        }
        return contents;
    }
   
    /**
     * This should go to utilities section 
     */
    public static String normalizeSpaces(String text) throws IOException {
        StringReader reader = new StringReader(text);
        StringBuffer buffer = new StringBuffer(text.length());
        int c, prev = -1;
        while( (c = reader.read()) != -1) {
            if (c == '\n' || c == '\r' || c == '\f' || c == '\t') {
                if (prev==' ' || prev=='\n' || prev=='\r' || prev=='\f' || prev=='\t') {
                    ;
                } else {
                    buffer.append(' ');
                }
            } else {
                if (c == ' ' && (prev==' ' || prev=='\n' || prev=='\r' || prev=='\f' || prev=='\t')) {
                    ;
                } else {
                    buffer.append((char)c);
                }
            }
            prev = c;
        }
        return buffer.toString();
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
    
        /**
     * Receives two object arrays and returns their elements merged in a single array,
     * with the elements from the first array placed before the elements from the
     * second array.
     */
    public static Object mergeArrays(Object[] firstList, Object[] secondList) {
        Class type = firstList.getClass().getComponentType();
        Object newList = java.lang.reflect.Array.newInstance(type, firstList.length + secondList.length);
        System.arraycopy(firstList, 0, newList, 0, firstList.length);
        System.arraycopy(secondList, 0, newList, firstList.length, secondList.length);   
        return newList;
    }

}
