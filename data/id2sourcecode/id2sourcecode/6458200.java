    private void copyXMPMetadata(PdfReader reader, PdfWriter writer) throws IOException {
        PdfObject xmpObject = PdfReader.getPdfObject(reader.getCatalog().get(PdfName.METADATA));
        if (xmpObject != null && xmpObject.isStream()) {
            byte[] xmpMetadata = PdfReader.getStreamBytesRaw((PRStream) xmpObject);
            writer.setXmpMetadata(xmpMetadata);
        }
    }
