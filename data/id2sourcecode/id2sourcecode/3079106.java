    AcroFields(PdfReader reader, PdfWriter writer) {
        this.reader = reader;
        this.writer = writer;
        if (writer instanceof PdfStamperImp) {
            append = ((PdfStamperImp) writer).isAppend();
        }
        fill();
    }
