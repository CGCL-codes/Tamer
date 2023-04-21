    public void mergeAndStampPdf(boolean resetStampEachPage, String[] in, String out, String stamp) throws Exception {
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfCopy writer = new PdfSmartCopy(document, baos);
        document.open();
        int stampPageNum = 1;
        PdfReader stampReader = new PdfReader(pdfContent.get(stamp));
        for (String element : in) {
            PdfReader documentReader = new PdfReader(pdfContent.get(element));
            for (int pageNum = 1; pageNum <= documentReader.getNumberOfPages(); pageNum++) {
                PdfImportedPage mainPage = writer.getImportedPage(documentReader, pageNum);
                PdfCopy.PageStamp pageStamp = writer.createPageStamp(mainPage);
                if (resetStampEachPage) stampReader = new PdfReader(pdfContent.get(stamp));
                PdfImportedPage stampPage = writer.getImportedPage(stampReader, stampPageNum++);
                pageStamp.getOverContent().addTemplate(stampPage, 0, 0);
                pageStamp.alterContents();
                writer.addPage(mainPage);
                if (stampPageNum > stampReader.getNumberOfPages()) stampPageNum = 1;
            }
        }
        writer.close();
        document.close();
        pdfContent.put(out, baos.toByteArray());
    }
