    public void addTemplateData(PdfWriter writer, Document doc, int templatePageNum) {
        PdfImportedPage templateData = writer.getImportedPage(reader, templatePageNum);
        createBurdenText(writer);
        canvas.addTemplate(templateData, 0, 0);
    }
