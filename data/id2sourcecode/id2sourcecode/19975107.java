    PdfReaderInstance(PdfReader reader, PdfWriter writer, ArrayList xrefObj, ArrayList pages) {
        this.reader = reader;
        this.xrefObj = xrefObj;
        this.pages = pages;
        this.writer = writer;
        file = reader.getSafeFile();
        myXref = new int[xrefObj.size()];
    }
