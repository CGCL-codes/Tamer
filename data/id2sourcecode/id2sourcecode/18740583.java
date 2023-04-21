    protected synchronized void internalWriteObject(OutputStream output) throws ContentEncodingException, IOException {
        try {
            DigestOutputStream dos = new DigestOutputStream(output, MessageDigest.getInstance(DEFAULT_CHECKSUM_ALGORITHM));
            writeObjectImpl(dos);
            dos.flush();
            _lastSaved = dos.getMessageDigest().digest();
            setDirty(false);
        } catch (NoSuchAlgorithmException e) {
            Log.warning("No pre-configured algorithm {0} available -- configuration error!", DEFAULT_CHECKSUM_ALGORITHM);
            throw new RuntimeException("No pre-configured algorithm " + DEFAULT_CHECKSUM_ALGORITHM + " available -- configuration error!");
        }
    }
