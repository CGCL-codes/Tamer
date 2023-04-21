    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        Rectangle pagesize = reader.getPageSizeWithRotation(1);
        Document document = new Document(pagesize);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        PdfContentByte content = writer.getDirectContent();
        PdfImportedPage page = writer.getImportedPage(reader, 1);
        float x, y;
        for (int i = 0; i < 16; i++) {
            x = -pagesize.getWidth() * (i % 4);
            y = pagesize.getHeight() * (i / 4 - 3);
            content.addTemplate(page, 4, 0, 0, 4, x, y);
            document.newPage();
        }
        document.close();
    }
