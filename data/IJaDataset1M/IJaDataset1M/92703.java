package eu.pisolutions.ocelot.document.catalog;

import eu.pisolutions.ocelot.document.io.DocumentNodeFactory;
import eu.pisolutions.ocelot.document.io.DocumentReadingContext;
import eu.pisolutions.ocelot.document.value.EnumWithNameNode;
import eu.pisolutions.ocelot.object.PdfObject;
import eu.pisolutions.ocelot.version.PdfVersion;

public final class PageModeNode extends EnumWithNameNode<PageMode> {

    public enum Factory implements DocumentNodeFactory<PageModeNode> {

        INSTANCE;

        public PageModeNode createNode(DocumentReadingContext context, boolean indirect, PdfObject object) {
            return new PageModeNode(indirect);
        }
    }

    public static PageModeNode valueOf(PageMode value) {
        return value != null ? new PageModeNode(value) : null;
    }

    public PageModeNode(PageMode value) {
        super(value);
    }

    private PageModeNode(boolean indirect) {
        super(indirect);
    }

    @Override
    public PdfVersion getRequiredPdfVersion() {
        return this.value.getRequiredPdfVersion();
    }

    @Override
    protected Class<PageMode> getEnumClass() {
        return PageMode.class;
    }
}
