    protected String readAheadUntilEndHtmlTagWithOpenBrace(InlineStringReader reader, InlineStringWriter writer) throws IOException, CleanerException {
        int oldChar = reader.getLastChar();
        int cur = reader.read();
        String nextTag = readAheadUntilEndHtmlTag(reader);
        reader.unread(cur);
        reader.setLastChar(oldChar);
        return nextTag;
    }
