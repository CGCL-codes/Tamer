    protected void jumpOverHtmlAttributeString(InlineStringReader reader, InlineStringWriter writer, int stringCharacter, boolean allowJumpToPhp) throws IOException, CleanerException {
        try {
            writer.enableWordwrap(false);
            writer.enableIndent(false);
            int cur = -1;
            while ((cur = reader.read()) != -1) {
                if (cur == stringCharacter) {
                    writer.write(cur);
                    return;
                }
                if (allowJumpToPhp && cur == '<' && reader.readAhead(4).equals("?php")) {
                    reader.unread(cur);
                    getInline().cleanPhpBlock(reader, writer);
                    continue;
                }
                writer.write(cur);
            }
            throw new InlineCleanerException("HTML Attribute string did not terminate", reader);
        } finally {
            writer.enableWordwrap(true);
            writer.enableIndent(true);
        }
    }
