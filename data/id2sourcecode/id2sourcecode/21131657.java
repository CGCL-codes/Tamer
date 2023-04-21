    public static void overlayPdf(List pdfDocuments, String fileNamePdfOutput) throws PdfRendererException {
        try {
            FileOutputStream fos = new FileOutputStream(fileNamePdfOutput);
            PdfReader baseReader = new PdfReader(((File) pdfDocuments.get(0)).getAbsolutePath());
            Rectangle pageSize = baseReader.getPageSize(1);
            Document document = new Document(pageSize);
            PdfWriter writer = PdfWriter.getInstance(document, fos);
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            document.newPage();
            for (Iterator it = pdfDocuments.iterator(); it.hasNext(); ) {
                File pdfFile = (File) it.next();
                PdfReader reader = new PdfReader(pdfFile.getAbsolutePath());
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
