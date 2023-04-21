    public String process(String name, Reader reader, Configuration cfg, Object model) throws IOException, TemplateException {
        StringWriter writer = new StringWriter();
        process(name, reader, cfg, model, writer, null);
        return writer.getBuffer().toString();
    }
