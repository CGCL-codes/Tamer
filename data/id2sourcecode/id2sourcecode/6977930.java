    public static void main(String[] args) throws IOException, DocumentException {
        new Superimposing().createPdf(SOURCE);
        PdfReader reader = new PdfReader(SOURCE);
        Document document = new Document(PageSize.POSTCARD);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
        document.open();
        PdfContentByte canvas = writer.getDirectContent();
        PdfImportedPage page;
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            page = writer.getImportedPage(reader, i);
            canvas.addTemplate(page, 1f, 0, 0, 1, 0, 0);
        }
        document.close();
    }
