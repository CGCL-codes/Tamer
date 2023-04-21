    protected static byte[] hmac(byte[] msg, byte[] key) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception ex) {
        }
        if (key.length > 64) {
            md5.reset();
            key = md5.digest(key);
        }
        byte ipad[] = new byte[64];
        System.arraycopy(key, 0, ipad, 0, key.length);
        byte opad[] = ipad.clone();
        for (int i = 0; i < ipad.length; i++) {
            ipad[i] ^= (byte) 0x36;
            opad[i] ^= (byte) 0x5c;
        }
        md5.reset();
        md5.update(ipad);
        byte digest[] = md5.digest(msg);
        md5.reset();
        md5.update(opad);
        return md5.digest(digest);
    }
