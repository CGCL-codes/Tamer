package beauty.beautifiers;

import java.io.*;
import java.util.*;
import beauty.parsers.ParserException;
import beauty.parsers.jsp.*;
import org.gjt.sp.jedit.jEdit;

public class JspBeautifier extends Beautifier {

    private static JspParser parser = null;

    public String beautify(String text) throws ParserException {
        try {
            StringReader is = new StringReader(text);
            if (parser == null) {
                parser = new JspParser(is);
            } else {
                parser.ReInit(is);
                parser.resetTokenSource();
            }
            parser.setIndentWidth(getIndentWidth());
            parser.setTabSize(getTabWidth());
            parser.setLineSeparator(getLineSeparator());
            parser.setPadSlashEnd(jEdit.getBooleanProperty("beauty.jsp.padSlashEnd", false));
            parser.setPadTagEnd(jEdit.getBooleanProperty("beauty.jsp.padTagEnd", false));
            parser.setWrapAttributes(jEdit.getBooleanProperty("beauty.jsp.wrapAttributes", false));
            parser.setCollapseBlankLines(jEdit.getBooleanProperty("beauty.jsp.collapseBlankLines", true));
            parser.parse();
            text = parser.getText();
            return text;
        } catch (Exception e) {
            throw new ParserException(e);
        }
    }
}
