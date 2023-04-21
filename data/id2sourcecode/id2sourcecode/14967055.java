    static final String HMAC_MD5(byte[] key, byte[] text) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        if (key.length > 64) {
            key = md5.digest(key);
        }
        byte[] ipad = new byte[MD5_BLOCKSIZE];
        byte[] opad = new byte[MD5_BLOCKSIZE];
        byte[] digest;
        int i;
        for (i = 0; i < MD5_BLOCKSIZE; i++) {
            for (; i < key.length; i++) {
                ipad[i] = key[i];
                opad[i] = key[i];
            }
            ipad[i] = 0x00;
            opad[i] = 0x00;
        }
        for (i = 0; i < MD5_BLOCKSIZE; i++) {
            ipad[i] ^= 0x36;
            opad[i] ^= 0x5c;
        }
        md5.update(ipad);
        md5.update(text);
        digest = md5.digest();
        md5.update(opad);
        md5.update(digest);
        digest = md5.digest();
        StringBuffer digestString = new StringBuffer();
        for (i = 0; i < digest.length; i++) {
            if ((digest[i] & 0x000000ff) < 0x10) {
                digestString.append("0" + Integer.toHexString(digest[i] & 0x000000ff));
            } else {
                digestString.append(Integer.toHexString(digest[i] & 0x000000ff));
            }
        }
        return (digestString.toString());
    }
