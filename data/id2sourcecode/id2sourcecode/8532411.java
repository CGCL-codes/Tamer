    public boolean selfTest() {
        if (valid == null) {
            byte[] key;
            try {
                key = "abcdefghijklmnop".getBytes("ASCII");
            } catch (UnsupportedEncodingException x) {
                throw new RuntimeException("ASCII not supported");
            }
            byte[] nonce = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7 };
            UMac32 mac = new UMac32();
            Map attributes = new HashMap();
            attributes.put(MAC_KEY_MATERIAL, key);
            attributes.put(NONCE_MATERIAL, nonce);
            try {
                mac.init(attributes);
            } catch (InvalidKeyException x) {
                x.printStackTrace(System.err);
                return false;
            }
            byte[] data = new byte[128];
            data[0] = (byte) 0x80;
            mac.update(data, 0, 128);
            byte[] result = mac.digest();
            valid = Boolean.valueOf(TV1.equals(Util.toString(result)));
        }
        return valid.booleanValue();
    }
