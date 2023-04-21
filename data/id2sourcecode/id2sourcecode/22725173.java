    public void cleanHtmlBlock(InlineStringReader reader, InlineStringWriter writer) throws IOException, CleanerException {
        Stack<String> tagStack = new Stack<String>();
        String lastTag = null;
        String stackTag = null;
        while (cleanHtmlTextUntilNextTag(stackTag, lastTag, reader, writer, '<') && reader.readAhead() != -1) {
            String next5 = reader.readAhead(5);
            if (next5.equals("<?xml")) {
                getInline().cleanXmlBlock(reader, writer);
            } else if (next5.equals("<?php")) {
                getInline().cleanPhpBlock(reader, writer);
            } else if (next5.substring(0, 4).equals("<!--")) {
                cleanHtmlComment(reader, writer);
            } else {
                lastTag = cleanHtmlTag(reader, writer).toLowerCase();
                stackTag = getCurrentTagFromStack(lastTag, tagStack);
            }
        }
    }
