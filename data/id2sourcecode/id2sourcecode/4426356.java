    public void useStationary(PdfWriter writer) throws IOException {
        writer.setPageEvent(this);
        PdfReader reader = new PdfReader(STATIONERY);
        page = writer.getImportedPage(reader, 1);
    }
