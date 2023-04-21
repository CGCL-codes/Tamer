    public static void main(String[] args) throws IOException, DocumentException {
        new Layers().createPdf(SOURCE);
        PdfReader reader = new PdfReader(SOURCE);
        Document document = new Document(PageSize.A5.rotate());
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
        document.open();
        PdfContentByte canvas = writer.getDirectContent();
        PdfImportedPage page;
        BaseFont bf = BaseFont.createFont(BaseFont.ZAPFDINGBATS, "", BaseFont.EMBEDDED);
        for (int i = 0; i < reader.getNumberOfPages(); ) {
            page = writer.getImportedPage(reader, ++i);
            canvas.addTemplate(page, 1f, 0, 0.4f, 0.4f, 72, 50 * i);
            canvas.beginText();
            canvas.setFontAndSize(bf, 20);
            canvas.showTextAligned(Element.ALIGN_CENTER, String.valueOf((char) (181 + i)), 496, 150 + 50 * i, 0);
            canvas.endText();
        }
        document.close();
    }
