    public Template process(String name, Reader reader, Configuration cfg, Object model, Writer writer, TemplateExceptionHandler templateExceptionHandler) throws IOException, TemplateException {
        Template template = null;
        try {
            template = new Template(name, reader, cfg);
            if (templateExceptionHandler != null) {
                cfg.setTemplateExceptionHandler(templateExceptionHandler);
            }
            if (model != null) {
                template.process(model, writer == null ? NullWriter.NULL_WRITER : writer);
            }
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        } finally {
            reader.close();
        }
        return template;
    }
