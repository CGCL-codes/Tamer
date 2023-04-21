    public Reader getUpdatedContents() throws Exception {
        StringWriter writer = new StringWriter();
        save(writer);
        writer.flush();
        writer.close();
        StringReader reader = new StringReader(writer.getBuffer().toString());
        return reader;
    }
