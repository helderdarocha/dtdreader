package argonavis.dtd.tagdata;

public class SequenceContentModel extends ContentModel {
    
    private char separator = ',';

    public SequenceContentModel(ContentModelItem[] contents) {
        super(contents, ONE);
    }
    
    public SequenceContentModel(ContentModelItem[] contents, String suffix) {
        super(contents, suffix);
    }
    
    public char getSeparator() {
        return separator;
    }

}

