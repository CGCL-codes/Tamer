    PdfStamperImp(PdfReader reader, OutputStream os, char pdfVersion, boolean append) throws DocumentException, IOException {
        super(new PdfDocument(), os);
        if (reader.isTampered()) throw new DocumentException("The original document was reused. Read it again from file.");
        reader.setTampered(true);
        this.reader = reader;
        file = reader.getSafeFile();
        this.append = append;
        if (append) {
            if (reader.isRebuilt()) throw new DocumentException("Append mode requires a document without errors even if recovery was possible.");
            if (reader.isEncrypted()) throw new DocumentException("Append mode requires a document without encryption.");
            HEADER = getISOBytes("\n");
            file.reOpen();
            byte buf[] = new byte[8192];
            int n;
            while ((n = file.read(buf)) > 0) this.os.write(buf, 0, n);
            file.close();
            prevxref = reader.getLastXref();
            reader.setAppendable(true);
        } else {
            if (pdfVersion == 0) super.setPdfVersion(reader.getPdfVersion()); else super.setPdfVersion(pdfVersion);
        }
        super.open();
        pdf.addWriter(this);
        if (append) {
            body.setRefnum(reader.getXrefSize());
            marked = new IntHashtable();
            if (reader.isNewXrefType()) fullCompression = true;
            if (reader.isHybridXref()) fullCompression = false;
        }
        initialXrefSize = reader.xrefObj.size();
    }
