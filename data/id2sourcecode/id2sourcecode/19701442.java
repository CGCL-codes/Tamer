    public boolean didSwitchToPhpMode(InlineStringReader reader, InlineStringWriter writer, int cur) throws IOException, CleanerException {
        if (cur == '<' && reader.readAhead(4).equals("?php")) {
            boolean oldIndent = writer.getIndentEnabled();
            writer.enableIndent(true);
            reader.unread('<');
            cleanPhpBlock(reader, writer);
            writer.enableIndent(oldIndent);
            return true;
        }
        return false;
    }
