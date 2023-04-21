    protected byte[] digestContent() throws ContentEncodingException, IOException {
        try {
            DigestOutputStream dos = new DigestOutputStream(new NullOutputStream(), MessageDigest.getInstance(DEFAULT_CHECKSUM_ALGORITHM));
            writeObjectImpl(dos);
            dos.flush();
            dos.close();
            byte[] currentValue = dos.getMessageDigest().digest();
            return currentValue;
        } catch (NoSuchAlgorithmException e) {
            Log.warning("No pre-configured algorithm {0} available -- configuration error!", DEFAULT_CHECKSUM_ALGORITHM);
            throw new RuntimeException("No pre-configured algorithm " + DEFAULT_CHECKSUM_ALGORITHM + " available -- configuration error!");
        }
    }
