    public static String getMD5(String source) {
        byte[] bytes;
        try {
            bytes = source.getBytes("UTF-8");
        } catch (java.io.UnsupportedEncodingException ue) {
            throw new IllegalStateException(ue);
        }
        byte[] result;
        synchronized (MESSAGE_DIGEST) {
            MESSAGE_DIGEST.update(bytes);
            result = MESSAGE_DIGEST.digest();
        }
        char[] resChars = new char[32];
        int len = result.length;
        for (int i = 0; i < len; i++) {
            byte b = result[i];
            int lo4 = b & 0x0F;
            int hi4 = (b & 0xF0) >> 4;
            resChars[i * 2] = HEX_CHARS.charAt(hi4);
            resChars[i * 2 + 1] = HEX_CHARS.charAt(lo4);
        }
        return new String(resChars);
    }
