    public static void concatPDFs(List<CustomPdfReader> readers, OutputStream outputStream, boolean paginate) {
        Document document = new Document();
        try {
            int totalPages = 0;
            Iterator<CustomPdfReader> iteratorPDFs = readers.iterator();
            while (iteratorPDFs.hasNext()) {
                PdfReader pdf = iteratorPDFs.next();
                totalPages += pdf.getNumberOfPages();
            }
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            PdfContentByte cb = writer.getDirectContent();
            PdfImportedPage page;
            int currentPageNumber = 0;
            int pageOfCurrentReaderPDF = 0;
            Iterator<CustomPdfReader> iteratorPDFReader = readers.iterator();
            while (iteratorPDFReader.hasNext()) {
                CustomPdfReader pdfReader = iteratorPDFReader.next();
                while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
                    document.newPage();
                    pageOfCurrentReaderPDF++;
                    currentPageNumber++;
                    page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
                    cb.addTemplate(page, 0, 0);
                    if (paginate) {
                        cb.beginText();
                        cb.setFontAndSize(bf, 9);
                        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "" + currentPageNumber + " of " + totalPages, 520, 5, 0);
                        cb.endText();
                    }
                    if (pdfReader.getTemplateFile() != null) {
                        PdfImportedPage headerPage = writer.getImportedPage(pdfReader.getTemplateFile(), 1);
                        writer.getDirectContentUnder().addTemplate(headerPage, 0, 0);
                    }
                }
                pageOfCurrentReaderPDF = 0;
            }
            outputStream.flush();
            document.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (document.isOpen()) document.close();
            try {
                if (outputStream != null) outputStream.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
