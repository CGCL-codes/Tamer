    public void jumpOverInlineComment(InlineStringReader reader, InlineStringWriter writer, boolean allowSwitchToPhpMode) throws IOException, CleanerException {
        try {
            writer.enableWordwrap(false);
            int cur = -1;
            while ((cur = reader.read()) != -1) {
                if (allowSwitchToPhpMode) {
                    if (getInline().didSwitchToPhpMode(reader, writer, cur)) {
                        continue;
                    }
                }
                if (cur == '\r') {
                    continue;
                }
                if (cur == '\n') {
                    writer.newLine();
                    return;
                }
                writer.write(cur);
            }
        } finally {
            writer.enableWordwrap(true);
        }
    }
