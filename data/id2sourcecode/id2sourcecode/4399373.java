    public static byte[] hmacMD5(byte[] data, byte[] key) throws Exception {
        byte[] ipad = new byte[64];
        byte[] opad = new byte[64];
        for (int i = 0; i < 64; i++) {
            if (i < key.length) {
                ipad[i] = (byte) (key[i] ^ 0x36);
                opad[i] = (byte) (key[i] ^ 0x5c);
            } else {
                ipad[i] = 0x36;
                opad[i] = 0x5c;
            }
        }
        byte[] content = new byte[data.length + 64];
        System.arraycopy(ipad, 0, content, 0, 64);
        System.arraycopy(data, 0, content, 64, data.length);
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        data = md5.digest(content);
        content = new byte[data.length + 64];
        System.arraycopy(opad, 0, content, 0, 64);
        System.arraycopy(data, 0, content, 64, data.length);
        return md5.digest(content);
    }
