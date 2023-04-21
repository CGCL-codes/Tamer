    public Throwable preview(Writer writer) throws IOException {
        writer.write("");
        IDocument document = getDocument();
        if (document == null) return null;
        String templateName = getTemplateName();
        String pageContents = document.get();
        try {
            Reader reader = new StringReader(pageContents);
            Object model = getModel();
            FreemarkerTemplateManager.getManager().process(templateName, reader, getConfiguration(), model, writer, TemplateExceptionHandler.RETHROW_HANDLER);
        } catch (Throwable e) {
            return e;
        } finally {
            writer.flush();
            writer.close();
        }
        return null;
    }
