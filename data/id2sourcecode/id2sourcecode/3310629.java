    public static byte[] getNTLMResponse(String password, byte[] challenge) {
        byte[] uni = null;
        byte[] p21 = new byte[21];
        byte[] p24 = new byte[24];
        try {
            uni = password.getBytes("UnicodeLittleUnmarked");
        } catch (UnsupportedEncodingException uee) {
            if (DebugFile.trace) new ErrorHandler(uee);
        }
        MD4 md4 = new MD4();
        md4.update(uni);
        try {
            md4.digest(p21, 0, 16);
        } catch (Exception ex) {
            if (DebugFile.trace) new ErrorHandler(ex);
        }
        E(p21, challenge, p24);
        return p24;
    }
