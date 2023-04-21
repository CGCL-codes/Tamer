    public void cleanXmlBlock(InlineStringReader reader, InlineStringWriter writer) throws IOException, CleanerException {
        writer.enableWordwrap(false);
        boolean inTag = false;
        boolean inString = false;
        while (true) {
            String next5 = reader.readAhead(5);
            if (next5 != null && next5.equals("<?php")) {
                boolean oldWordWrap = writer.canWordWrap();
                writer.enableWordwrap(true);
                getInline().cleanPhpBlock(reader, writer);
                writer.enableWordwrap(oldWordWrap);
            } else {
                int c = reader.read();
                if (c == -1) {
                    break;
                } else if (!inTag && c == '<') {
                    inTag = true;
                    writer.enableWordwrap(true);
                } else if (inTag && c == '>') {
                    inTag = false;
                    writer.enableWordwrap(false);
                } else if (inTag && !inString && c == '"') {
                    inString = true;
                    writer.enableWordwrap(false);
                } else if (inTag && inString && c == '"') {
                    inString = false;
                    writer.enableWordwrap(true);
                }
                writer.write(c);
            }
        }
        writer.enableWordwrap(true);
    }
