    protected void jumpOverJavascriptRegexp(InlineStringReader reader, InlineStringWriter writer, boolean allowSwitchToPhpMode) throws IOException, CleanerException {
        try {
            writer.enableIndent(false);
            int cur = -1;
            while ((cur = reader.read()) != -1) {
                if (allowSwitchToPhpMode) {
                    if (getInline().didSwitchToPhpMode(reader, writer, cur)) {
                        continue;
                    }
                }
                if (cur == '/') {
                    writer.write(cur);
                    while ((cur = reader.read()) != -1) {
                        if (!Character.isLetter(cur)) break;
                        writer.write(cur);
                    }
                    reader.unread(cur);
                    return;
                }
                writer.write(cur);
                if (cur == '\\' && reader.readAhead() == '/') {
                    writer.write(reader.read());
                }
            }
            throw new InlineCleanerException("PHP single-quoted string did not terminate", reader);
        } finally {
            writer.enableIndent(true);
        }
    }
