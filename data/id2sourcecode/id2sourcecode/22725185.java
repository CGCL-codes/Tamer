    protected boolean cleanHtmlTextUntilNextTag(String currentTag, String lastTag, InlineStringReader reader, InlineStringWriter writer, char c) throws IOException, CleanerException {
        int cur;
        int prev = -1;
        boolean addWhitespace = false;
        boolean hasDoneWhitespace = false;
        while ((cur = reader.readAhead()) != -1) {
            if (cur == '<') {
                if (addWhitespace && !Character.isWhitespace(writer.getPrevious())) {
                    String nextTag = getInline().readAheadUntilEndHtmlTagWithOpenBrace(reader, writer);
                    if (!htmlTagRequiresInlineWhitespace(currentTag) || htmlTagWillIgnoreLeadingWhitespace(lastTag) || htmlTagWillIgnoreTrailingWhitespace(nextTag)) {
                        if ((nextTag.isEmpty() || (nextTag.charAt(0) != '/' && nextTag.charAt(0) != '!')) && !htmlTagNeedsNewLine(nextTag)) {
                            writer.write(' ');
                            addWhitespace = false;
                        }
                    } else {
                        if (!htmlTagWillIgnoreLeadingWhitespace(nextTag) || !htmlTagWillIgnoreTrailingWhitespace(nextTag)) {
                            if (nextTag.isEmpty() || nextTag.charAt(0) != '!') {
                                if (!htmlTagWillIgnoreAllWhitespace(currentTag)) {
                                    writer.write(' ');
                                    addWhitespace = false;
                                }
                            }
                        }
                    }
                }
                return true;
            }
            reader.read();
            if (Character.isWhitespace(cur)) {
                if (!hasDoneWhitespace && htmlTagRequiresInlineWhitespace(currentTag) && !htmlTagWillIgnoreLeadingWhitespace(lastTag) && !Character.isWhitespace(writer.getPrevious())) {
                    addWhitespace = true;
                } else if (Character.isWhitespace(prev)) {
                } else if (prev == -1) {
                } else if (reader.readAhead() == '<' && (reader.readAhead(2).charAt(1) == '/' || reader.readAhead(2).charAt(1) == '!')) {
                } else {
                    addWhitespace = true;
                }
            } else {
                if (addWhitespace) {
                    writer.write(' ');
                    addWhitespace = false;
                }
                writer.write(cur);
            }
            prev = cur;
        }
        return false;
    }
