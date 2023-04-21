    public Reader merge() throws Exception {
        StringWriter writer = new StringWriter();
        merge(writer);
        writer.flush();
        writer.close();
        StringReader reader = new StringReader(writer.getBuffer().toString());
        return reader;
    }
