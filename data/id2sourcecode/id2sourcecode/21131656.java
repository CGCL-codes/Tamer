    public static void overlayPdf(File[] pdfDocuments, String fileNamePdfOutput) throws PdfRendererException {
        try {
            FileOutputStream fos = new FileOutputStream(fileNamePdfOutput);
            PdfReader baseReader = new PdfReader(pdfDocuments[0].getAbsolutePath());
            Rectangle pageSize = baseReader.getPageSize(1);
            Document document = new Document(pageSize);
            PdfWriter writer = PdfWriter.getInstance(document, fos);
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            document.newPage();
            for (int i = 0; i < pdfDocuments.length; i++) {
                PdfReader reader = new PdfReader(pdfDocuments[i].getAbsolutePath());
                PdfImportedPage page = writer.getImportedPage(reader, 1);
                cb.addTemplate(page, 0, 0);
            }
            document.close();
        } catch (IOException e) {
            String error = "Ha ocurrido un error al leer los ficheros pdf";
            throw new PdfRendererException(error, e);
        } catch (DocumentException e) {
            String error = "Ha ocurrido un error al procesar los ficheros pdf";
            throw new PdfRendererException(error, e);
        }
    }
