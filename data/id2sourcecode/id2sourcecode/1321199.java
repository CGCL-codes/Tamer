    private byte[] calculateChecksum(InputStream input) {
        DigestInputStream in = null;
        byte[] digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance(getProperty(XMLibrarian.PROP_DIGEST));
            in = new DigestInputStream(input, md);
            readStream(in, BUFFER);
            digest = md.digest();
        } catch (Exception e) {
            handleException(e, null);
        } finally {
            close(in);
            return digest;
        }
    }
