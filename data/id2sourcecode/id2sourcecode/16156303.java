    public static void main(String[] args) {
        System.out.println("Chapter 2: example HelloWorldWriter");
        System.out.println("-> Creates a PDF file, then 'folds' it;");
        System.out.println("   4 pages are copied on 1 in a specific order.");
        System.out.println("-> jars needed: iText.jar");
        System.out.println("-> files generated in /results subdirectory:");
        System.out.println("   HelloWorldToImport.pdf");
        System.out.println("   HelloWorldFolded.pdf");
        createPdf("HelloWorldToImport.pdf");
        Document document = new Document(PageSize.A4);
        try {
            PdfReader reader = new PdfReader("HelloWorldToImport.pdf");
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("HelloWorldFolded.pdf"));
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            PdfImportedPage page;
            page = writer.getImportedPage(reader, 1);
            cb.addTemplate(page, -0.5f, 0f, 0f, -0.5f, PageSize.A4.getWidth() / 2, PageSize.A4.getHeight());
            page = writer.getImportedPage(reader, 2);
            cb.addTemplate(page, 0.5f, 0f, 0f, 0.5f, 0f, 0f);
            page = writer.getImportedPage(reader, 3);
            cb.addTemplate(page, 0.5f, 0f, 0f, 0.5f, PageSize.A4.getWidth() / 2f, 0f);
            page = writer.getImportedPage(reader, 4);
            cb.addTemplate(page, -0.5f, 0f, 0f, -0.5f, PageSize.A4.getWidth(), PageSize.A4.getHeight());
            cb.setLineDash(20, 10, 10);
            cb.moveTo(0, PageSize.A4.getHeight() / 2f);
            cb.lineTo(PageSize.A4.getWidth(), PageSize.A4.getHeight() / 2f);
            cb.stroke();
            cb.moveTo(PageSize.A4.getWidth() / 2f, 0);
            cb.lineTo(PageSize.A4.getWidth() / 2f, PageSize.A4.getHeight());
            cb.stroke();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.close();
    }
