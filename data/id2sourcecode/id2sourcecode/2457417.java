    private String readReasource(String systemId) throws UnknownIOSourceException, IOException, InputOutputException {
        InputOutput io = InputOutput.create(systemId);
        Reader reader = new InputStreamReader(io.getBufferedInputStream());
        try {
            StringWriter writer = new StringWriter();
            try {
                Stream.readTo(reader, writer);
                return writer.toString();
            } finally {
                writer.close();
            }
        } finally {
            reader.close();
        }
    }
