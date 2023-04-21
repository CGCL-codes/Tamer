    public void generate(Writer writer, Reader reader) {
        HTMLWriter htmlWriter = null;
        try {
            htmlWriter = getHTMLWriter(reader);
        } catch (Exception e) {
        }
        htmlWriter.addDocumentFragmentFilter(new SpanFilter());
        try {
            htmlWriter.write(writer);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
