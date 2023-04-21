    public InputStream addBackground(InputStream mainDocument, InputStream backgroundDocument, int[] pages) throws Exception {
        log.debug("Adding a background.");
        PdfReader mainReader = new PdfReader(mainDocument);
        PdfReader backgroundReader = new PdfReader(backgroundDocument);
        com.lowagie.text.Document document = new com.lowagie.text.Document(backgroundReader.getPageSizeWithRotation(1));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, out);
        document.open();
        PdfContentByte cb = writer.getDirectContent();
        PdfImportedPage backgroundPage = writer.getImportedPage(backgroundReader, 1);
        int numMainPages = mainReader.getNumberOfPages();
        boolean[] include = getIncludeBackground(pages, numMainPages);
        for (int i = 1; i <= numMainPages; i++) {
            document.newPage();
            if (include[i - 1]) cb.addTemplate(backgroundPage, 0, 0);
            PdfImportedPage mainPage = writer.getImportedPage(mainReader, i);
            cb.addTemplate(mainPage, 0, 0);
        }
        document.close();
        backgroundReader.close();
        mainReader.close();
        out.flush();
        return new ByteArrayInputStream(out.toByteArray());
    }
