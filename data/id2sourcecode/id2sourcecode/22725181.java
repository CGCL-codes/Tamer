    protected String cleanHtmlTag(InlineStringReader reader, InlineStringWriter writer) throws IOException, CleanerException {
        int first = reader.read();
        String tagName = findHtmlTagName(reader);
        if (tagName.isEmpty()) {
            throw new InlineCleanerException("Unexpectedly hit an invalid HTML tag while searching for HTML tags [first='" + (char) first + "']", reader);
        }
        if (tagName.charAt(0) == '/' && htmlTagIndented(tagName)) {
            writer.indentDecrease();
            writer.newLineMaybe();
        }
        if (htmlTagNeedsNewLine(tagName)) {
            writer.newLineMaybe();
        }
        writer.write(first);
        int cur;
        boolean doneTag = false;
        while ((cur = reader.read()) != -1) {
            if (cur == '>') {
                int prev = writer.getPrevious();
                writer.write(cur);
                if (tagName.charAt(0) != '/' && htmlTagIndented(tagName)) {
                    writer.indentIncrease();
                    writer.newLine();
                }
                if (htmlTagNeedsTrailingNewLine(tagName)) {
                    writer.newLine();
                } else if (prev == '/' && htmlTagNeedsNewLineSingleton(tagName)) {
                    writer.newLine();
                }
                if (tagName.toLowerCase().equals("script")) {
                    getInline().cleanHtmlJavascript(reader, writer, true);
                } else if (tagName.toLowerCase().equals("style")) {
                    getInline().cleanHtmlCss(reader, writer, true);
                }
                return tagName;
            } else if (Character.isWhitespace(cur)) {
                if (!doneTag) {
                } else {
                    reader.unread(cur);
                    cleanHtmlTagAttributes(reader, writer);
                }
            } else {
                writer.write(cur);
                if (Character.isLetterOrDigit(cur) || cur == '_') {
                    doneTag = true;
                }
            }
        }
        getInline().throwWarning("We never found the end of HTML tag", tagName.toString());
        return null;
    }
