    protected String cleanScript(InlineStringReader reader, String extension) throws CleanerException {
        InlineStringWriter writer = new IACleanerStringWriter(this);
        if (wordWrapLength != -1) {
            writer.setWordWrapLength(wordWrapLength);
        }
        extension = extension.toLowerCase();
        try {
            if (extension.equals("js")) {
                cleanHtmlJavascript(reader, writer, false);
            } else if (extension.equals("css")) {
                cleanHtmlCss(reader, writer, false);
            } else if (extension.equals("txt") || extension.equals("tex")) {
                writer.enableWordwrap(false);
                directCopy(reader, writer);
                writer.enableWordwrap(true);
            } else {
                cleanHtmlBlock(reader, writer);
            }
        } catch (IOException e) {
            throw new CleanerException(e);
        }
        {
            int c;
            try {
                while ((c = reader.read()) != -1) {
                    writer.write(c);
                }
            } catch (IOException e) {
                throw new CleanerException(e);
            }
        }
        return writer.getBuffer().toString();
    }
