    protected void cleanHtmlComment(InlineStringReader reader, InlineStringWriter writer) throws IOException, CleanerException {
        int cur = -1;
        boolean isNewline = false;
        boolean startedComment = false;
        boolean commentLine = false;
        while (true) {
            if (!startedComment && Character.isWhitespace(reader.getLastChar())) {
                writer.newLineMaybe();
                commentLine = true;
            }
            startedComment = true;
            cur = reader.read();
            if (cur == -1) break;
            if (cur != '\n' && Character.isWhitespace(cur) && isNewline) {
            } else if (cur == '\r') {
            } else if (cur == '\n') {
                writer.newLine();
            } else {
                writer.write(cur);
                isNewline = false;
            }
            if (reader.readAhead(3).equals("-->")) {
                writer.write(reader.read(3));
                if (commentLine) writer.newLine();
                return;
            }
            if (cur == '\n') {
                isNewline = true;
            }
        }
        throw new CleanerException("At end of file before found end of comment");
    }
