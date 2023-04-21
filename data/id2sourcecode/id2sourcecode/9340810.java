    private byte[] calcNTHash() throws GeneralSecurityException {
        byte[] pw = null;
        try {
            pw = password.getBytes("UnicodeLittleUnmarked");
        } catch (UnsupportedEncodingException e) {
            assert false;
        }
        byte[] out = md4.digest(pw);
        byte[] result = new byte[21];
        System.arraycopy(out, 0, result, 0, 16);
        return result;
    }
