package nl.flotsam.preon.util;

import nl.flotsam.limbo.Document;
import nl.flotsam.pecia.ParaContents;

public class DocumentParaContents implements Document {

    private ParaContents<?> contents;

    public DocumentParaContents(ParaContents<?> contents) {
        this.contents = contents;
    }

    public Document detail(String text) {
        return null;
    }

    public void link(Object object, String text) {
        contents.link(object, text);
    }

    public void text(String text) {
        contents.text(text);
    }
}
