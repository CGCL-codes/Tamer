    private String computeChecksum(String csType, InputStream is) throws Exception {
        MessageDigest md = MessageDigest.getInstance(csType);
        if (is == null) {
            throw new IllegalArgumentException("InputStream cannot be null");
        }
        byte buffer[] = new byte[5000];
        int numread;
        while ((numread = is.read(buffer, 0, 5000)) > 0) {
            md.update(buffer, 0, numread);
        }
        return StringUtility.byteArraytoHexString(md.digest());
    }
