    private byte[] readContentBytes(final PdfObject contentObject) throws IOException {
        final byte[] result;
        switch(contentObject.type()) {
            case PdfObject.INDIRECT:
                final PRIndirectReference ref = (PRIndirectReference) contentObject;
                final PdfObject directObject = PdfReader.getPdfObject(ref);
                result = readContentBytes(directObject);
                break;
            case PdfObject.STREAM:
                final PRStream stream = (PRStream) PdfReader.getPdfObject(contentObject);
                result = PdfReader.getStreamBytes(stream);
                break;
            case PdfObject.ARRAY:
                final ByteArrayOutputStream allBytes = new ByteArrayOutputStream();
                final PdfArray contentArray = (PdfArray) contentObject;
                final ListIterator iter = contentArray.listIterator();
                while (iter.hasNext()) {
                    final PdfObject element = (PdfObject) iter.next();
                    allBytes.write(readContentBytes(element));
                }
                result = allBytes.toByteArray();
                break;
            default:
                final String msg = "Unable to handle Content of type " + contentObject.getClass();
                throw new IllegalStateException(msg);
        }
        return result;
    }
