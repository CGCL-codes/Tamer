    public VerifyingInputStream verifyStream(InputStream data, Storables storables, long transLength) throws DataNotValidIOException {
        if (!storables.isLegalForCHK()) {
            throw new DataNotValidIOException(Presentation.CB_BAD_KEY);
        }
        int log2size = val[20];
        long partSize = storables.getPartSize();
        long dataLength = getDataLength(transLength, partSize);
        if (log2size < LOG2_MINSIZE || log2size > LOG2_MAXSIZE || 1 << log2size != dataLength || partSize != getPartSize(dataLength)) {
            throw new DataNotValidIOException(Presentation.CB_BAD_KEY);
        }
        Digest ctx = SHA1.getInstance();
        storables.hashUpdate(ctx);
        if (!Util.byteArrayEqual(ctx.digest(), val, 0, 20)) {
            throw new DataNotValidIOException(Presentation.CB_BAD_KEY);
        }
        return super.verifyStream(data, storables, transLength);
    }
