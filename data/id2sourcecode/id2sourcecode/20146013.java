    public DocumentWriter createDocumentWriter(byte compressType) throws UnsupportedOperationException {
        if (compressType != COMPRESS_TYPE_NONE) {
            throw new UnsupportedOperationException("Compression is not supported for document writer, only for document reader");
        }
        return new DocParserImpl(compressType);
    }
