    protected void cleanHtmlTagAttributes(InlineStringReader reader, InlineStringWriter writer) throws IOException, CleanerException {
        int cur = -1;
        int prev = -1;
        boolean needWhitespace = false;
        boolean ignoreWhitespaceAfter = false;
        while ((cur = reader.read()) != -1) {
            if (Character.isWhitespace(cur)) {
                if (prev == -1) {
                    needWhitespace = true;
                } else if (Character.isWhitespace(prev)) {
                } else if (ignoreWhitespaceAfter) {
                } else {
                    needWhitespace = true;
                }
            } else if (cur == '=') {
                writer.write(cur);
                needWhitespace = false;
                ignoreWhitespaceAfter = true;
            } else if (cur == '"' || cur == '\'') {
                if (needWhitespace) {
                    writer.write(' ');
                }
                writer.write(cur);
                jumpOverHtmlAttributeString(reader, writer, cur, true);
                ignoreWhitespaceAfter = false;
                needWhitespace = true;
            } else if (cur == '<' && "?php".equals(reader.readAhead(4))) {
                if (needWhitespace) {
                    writer.write(' ');
                    needWhitespace = false;
                }
                reader.unread(cur);
                getInline().cleanPhpBlock(reader, writer);
            } else {
                if (needWhitespace) {
                    writer.write(' ');
                    needWhitespace = false;
                }
                ignoreWhitespaceAfter = false;
                writer.write(cur);
            }
            prev = cur;
            if (reader.readAhead() == '>') {
                return;
            }
        }
        throw new InlineCleanerException("Expected > to end HTML tag while parsing for attributes", reader);
    }
