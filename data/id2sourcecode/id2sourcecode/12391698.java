    public PdfTemplate createTemplate(PdfReader reader, int page) {
        return writer.getImportedPage(reader, page);
    }
