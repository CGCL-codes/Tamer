    private static byte[] hmac_md5(byte[] key, byte[] text) throws NoSuchAlgorithmException {
        byte[] k_ipad = new byte[64];
        byte[] k_opad = new byte[64];
        byte[] digest;
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        if (key.length > 64) {
            md5.update(key);
            key = md5.digest();
        }
        System.arraycopy(key, 0, k_ipad, 0, key.length);
        System.arraycopy(key, 0, k_opad, 0, key.length);
        for (int i = 0; i < 64; i++) {
            k_ipad[i] ^= 0x36;
            k_opad[i] ^= 0x5c;
        }
        md5.reset();
        md5.update(k_ipad);
        md5.update(text);
        digest = md5.digest();
        md5.reset();
        md5.update(k_opad);
        md5.update(digest);
        digest = md5.digest();
        return digest;
    }
