package wheel.components;

import org.xmlpull.v1.XmlSerializer;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Almost identical to Label-component. The difference is that Text-component can handle entities (like &amp;copy;) and
 * a Text-component can be set to CDATA-mode where its content will be enclosed in a CDATA-section in the rendered document.
 * For this reason, Text-component is also a bit more processing intensive so it should only be used when either entities or cdata is needed.
 * By default will render the text inside a &lt;p&gt; tag. Text-component also supports html-mode (call setHtml(true) to activate) that will
 * retain all html tags in the text in the resulting xhtml output.
 *
 * @author Henri Frilund
 */
public class Text extends RenderableComponent {

    private static Pattern entityMatcher;

    private static Pattern htmlMatcher;

    private static Pattern attributeMatcher;

    private CharSequence text;

    private boolean cdata;

    private boolean html;

    Text(Component parent, Object text) {
        super(parent);
        this.text = eval(text);
        renderSelf = false;
    }

    public String defaultTagName() {
        return null;
    }

    @Override
    public void renderComponent(XmlSerializer serializer) throws IOException {
        if (!cdata) {
            text = eval(text);
            if (html) {
                if (htmlMatcher == null) htmlMatcher = Pattern.compile("<([^<]+)>");
                Matcher matcher = htmlMatcher.matcher(text);
                int textPos = 0;
                while (matcher.find()) {
                    String htmlMatch = matcher.group(1);
                    int matchPos = matcher.start();
                    String precedingText = text.toString().substring(textPos, matchPos);
                    textPos += precedingText.length() + htmlMatch.length() + 2;
                    if (precedingText.length() > 0) handleEntities(precedingText, serializer);
                    int tagNameEndPos = 0;
                    char c = 0;
                    while (c != ' ' && c != '>' && tagNameEndPos < htmlMatch.length()) c = htmlMatch.charAt(tagNameEndPos++);
                    String htmlTag = htmlMatch.substring(0, tagNameEndPos).trim();
                    if (htmlTag.startsWith("/")) serializer.endTag("", htmlTag.substring(1, htmlTag.length())); else serializer.startTag("", htmlTag);
                    if (attributeMatcher == null) attributeMatcher = Pattern.compile("(\\w+)\\s*=\\s*[\\\\\"\\']([^\\\\\"\\']*)[\\\\\"\\']");
                    Matcher attrMatcher = attributeMatcher.matcher(htmlMatch);
                    while (attrMatcher.find()) {
                        String attrName = attrMatcher.group(1);
                        String attrValue = attrMatcher.group(2).replace("&amp;", "&");
                        serializer.attribute("", attrName, attrValue);
                    }
                    if (htmlMatch.endsWith("/")) serializer.endTag("", htmlTag);
                }
                if (textPos < text.length()) handleEntities(text.toString().substring(textPos, text.length()), serializer);
            } else {
                handleEntities(eval(text), serializer);
            }
        } else serializer.cdsect(eval(text));
    }

    public boolean isCdata() {
        return cdata;
    }

    public void setCdata(boolean cdata) {
        this.cdata = cdata;
    }

    public boolean isHtml() {
        return html;
    }

    public void setHtml(boolean html) {
        this.html = html;
    }

    private void handleEntities(String text, XmlSerializer serializer) throws IOException {
        if (text.contains("&") && getPage().renderDoctype) {
            if (entityMatcher == null) entityMatcher = Pattern.compile("(\\&\\w+;)");
            Matcher matcher = entityMatcher.matcher(text);
            int textPos = 0;
            while (matcher.find()) {
                String entity = matcher.group(1);
                int matchPos = matcher.start();
                String precedingText = text.substring(textPos, matchPos);
                textPos += precedingText.length() + entity.length();
                if (precedingText.length() > 0) serializer.text(precedingText);
                serializer.entityRef(entity.substring(1, entity.length() - 1));
            }
            if (textPos < text.length()) serializer.text(text.substring(textPos, text.length()));
        } else {
            serializer.text(text);
        }
    }
}
