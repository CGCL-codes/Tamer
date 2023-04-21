    public void addPDF(String source, String title) throws IOException, DocumentException {
        int lastString = source.indexOf("_");
        String temPath = null;
        if (lastString != -1) {
            temPath = source.substring(1, lastString);
        }
        String sourcePath = getFileRealPath(temPath).replaceAll("\\\\", "/");
        PdfReader reader = new PdfReader(sourcePath);
        PdfContentByte cb = writer.getDirectContent();
        int pageOfCurrentReaderPDF = 0;
        int pages = reader.getNumberOfPages();
        for (int i = 0; i < pages; i++) {
            document.newPage();
            pageOfCurrentReaderPDF++;
            PdfImportedPage page = writer.getImportedPage(reader, pageOfCurrentReaderPDF);
            cb.addTemplate(page, 0, 0);
        }
    }
