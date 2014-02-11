package argonavis.dtd.tagdata;

/**
 */
public class ChoiceContentModel extends ContentModel {
    
    private char separator = '|';

    public ChoiceContentModel(ContentModelItem[] contents) {
        super(contents, ONE);
    }
    
    public ChoiceContentModel(ContentModelItem[] contents, String suffix) {
        super(contents, suffix);
    }

    public char getSeparator() {
        return separator;
    }

}

