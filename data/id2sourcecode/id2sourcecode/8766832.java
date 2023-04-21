    public static String getChecksum(byte[] file) {
        String result = null;
        try {
            MessageDigest mdAlgorithm = MessageDigest.getInstance("MD5");
            mdAlgorithm.update(file);
            byte[] checksum = mdAlgorithm.digest();
            StringBuffer buffer = new StringBuffer(checksum.length * 2);
            for (byte b : checksum) {
                buffer.append(Integer.toHexString((b & 0xF0) >> 4));
                buffer.append(Integer.toHexString(b & 0x0F));
            }
            result = buffer.toString().toUpperCase();
        } catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
        }
        return result;
    }
