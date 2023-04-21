    PdfImportedPage(PdfReaderInstance readerInstance, PdfWriter writer, int pageNumber) {
        this.readerInstance = readerInstance;
        this.pageNumber = pageNumber;
        thisReference = writer.getPdfIndirectReference();
        bBox = readerInstance.getReader().getPageSize(pageNumber);
        type = TYPE_IMPORTED;
    }
