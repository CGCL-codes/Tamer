    public void jumpOverBlockComment(InlineStringReader reader, InlineStringWriter writer, boolean allowSwitchToPhpMode) throws IOException, CleanerException {
        try {
            writer.enableWordwrap(false);
            int cur = -1;
            boolean isBlankLine = false;
            boolean isJavadoc = (reader.readAhead() == '*');
            while ((cur = reader.read()) != -1) {
                if (allowSwitchToPhpMode) {
                    if (getInline().didSwitchToPhpMode(reader, writer, cur)) {
                        continue;
                    }
                }
                if (cur == '*' && reader.readAhead() == '/') {
                    if (cur == '*' && isBlankLine && isJavadoc) {
                        writer.write(' ');
                    }
                    writer.write(cur);
                    writer.write(reader.read());
                    return;
                }
                if (cur == '\r') {
                    continue;
                }
                if (Character.isWhitespace(cur) && isBlankLine) {
                } else {
                    if (cur == '*' && isBlankLine && isJavadoc) {
                        writer.write(' ');
                    }
                    writer.write(cur);
                    if (cur == '\n') {
                        isBlankLine = true;
                    } else {
                        isBlankLine = false;
                    }
                }
            }
            throw new InlineCleanerException("At end of file before found end of PHP block comment", reader);
        } finally {
            writer.enableWordwrap(true);
        }
    }
