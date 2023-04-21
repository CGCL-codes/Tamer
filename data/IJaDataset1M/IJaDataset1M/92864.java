package watij.elements;

import org.w3c.dom.Element;
import watij.finders.TagFinder;
import java.util.List;

public class TableCells extends HtmlElementCollections<TableCell> {

    public TableCells(List<Element> list, HtmlElementFactory htmlElementFactory) throws Exception {
        super(list, htmlElementFactory);
    }

    protected TableCell get(Element element) throws Exception {
        return htmlElementFactory().tableCell(element);
    }

    public boolean matches(Element element) throws Exception {
        return new TagFinder("td").matches(element) || new TagFinder("th").matches(element);
    }
}
