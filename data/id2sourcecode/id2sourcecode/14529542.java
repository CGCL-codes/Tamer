    public static byte[] buildTransferPayload(long segment, byte[] data) {
        byte[] payload = new byte[data.length + 24];
        System.arraycopy(Conversion.longToBytes(segment), 0, payload, 16, 8);
        System.arraycopy(data, 0, payload, 24, data.length);
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            System.err.println(ex);
            System.exit(1);
        }
        md.update(payload, 16, payload.length - 16);
        byte[] md5 = md.digest();
        System.arraycopy(md5, 0, payload, 0, 16);
        return payload;
    }
