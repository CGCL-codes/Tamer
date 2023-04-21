    public static byte[] getNTLMResponse(String password, byte[] challenge) {
        byte[] uni = null;
        byte[] p21 = new byte[21];
        byte[] p24 = new byte[24];
        try {
            uni = password.getBytes("UnicodeLittleUnmarked");
        } catch (UnsupportedEncodingException uee) {
            if (log.level > 0) uee.printStackTrace(log);
        }
        MD4 md4 = new MD4();
        md4.update(uni);
        try {
            md4.digest(p21, 0, 16);
        } catch (Exception ex) {
            if (log.level > 0) ex.printStackTrace(log);
        }
        E(p21, challenge, p24);
        return p24;
    }
