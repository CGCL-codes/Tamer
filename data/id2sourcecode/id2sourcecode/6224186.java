    public String replace(String s) {
        StringReader reader = new StringReader(s);
        StringWriter writer = new StringWriter();
        try {
            processStreams(reader, writer);
            return writer.toString();
        } catch (IOException ex) {
            throw new RuntimeException(ex.toString());
        }
    }
