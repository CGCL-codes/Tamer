    static byte[] createHMac(final char[] passwd, final byte[] data) throws InvalidKeyException, SaslException {
        final IMac mac = HMacFactory.getInstance(Registry.HMAC_NAME_PREFIX + Registry.MD5_HASH);
        final HashMap map = new HashMap();
        final byte[] km;
        try {
            km = new String(passwd).getBytes("UTF-8");
        } catch (UnsupportedEncodingException x) {
            throw new SaslException("createHMac()", x);
        }
        map.put(IMac.MAC_KEY_MATERIAL, km);
        mac.init(map);
        mac.update(data, 0, data.length);
        return mac.digest();
    }
