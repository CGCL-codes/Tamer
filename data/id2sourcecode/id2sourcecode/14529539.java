    public Chunk(byte[] payload) throws ChecksumException {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            System.err.println(ex);
            System.exit(1);
        }
        md.update(payload, 16, payload.length - 16);
        byte[] md5 = md.digest();
        byte[] payloadMd5 = new byte[16];
        System.arraycopy(payload, 0, payloadMd5, 0, 16);
        if (!Arrays.equals(md5, payloadMd5)) {
            throw new ChecksumException();
        }
        segment = Conversion.bytesToLong(payload, 16);
        data = new byte[payload.length - 24];
        System.arraycopy(payload, 24, data, 0, payload.length - 24);
    }
