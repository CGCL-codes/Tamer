    public static byte[] hmac_md5(byte[] text, byte[] key) {
        int minKeyLen = 64;
        byte[] digest = new byte[16];
        if (key.length < minKeyLen) {
            byte[] t = new byte[minKeyLen];
            System.arraycopy(key, 0, t, 0, key.length);
            key = t;
        }
        IMac mac = MacFactory.getInstance("HMAC-MD5");
        HashMap attributes = new HashMap();
        attributes.put(IMac.MAC_KEY_MATERIAL, key);
        attributes.put(IMac.TRUNCATED_SIZE, new Integer(16));
        try {
            mac.init(attributes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mac.update(text, 0, text.length);
        System.arraycopy(mac.digest(), 0, digest, 0, 16);
        return digest;
    }
