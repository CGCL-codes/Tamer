    public String encrypt(String str) throws Exception {
        try {
            byte[] utf8 = str.getBytes("UTF8");
            byte[] digest = md.digest(utf8);
            assert digest.length == 16 : "" + digest.length;
            byte[] all = new byte[utf8.length + digest.length];
            System.arraycopy(utf8, 0, all, 0, utf8.length);
            System.arraycopy(digest, 0, all, utf8.length, digest.length);
            byte[] enc = ecipher.doFinal(all);
            String ret = ZBase64Util.encodeBytes(enc, ZBase64Util.DONT_BREAK_LINES);
            return ret;
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }
    }
