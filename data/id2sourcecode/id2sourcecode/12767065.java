    public void copyText(Reader reader, final StringWriter writer) throws IOException {
        BufferedReader bufReader;
        String line;
        LineProcessor processor;
        bufReader = new BufferedReader(reader);
        try {
            processor = new LineProcessor() {

                public boolean processLine(String line, int lineNo) {
                    if (lineNo > 1) writer.write(LINE_SEPARATOR);
                    writer.write(line);
                    return true;
                }
            };
            this.processTextLines(bufReader, processor);
        } finally {
            bufReader.close();
        }
    }
