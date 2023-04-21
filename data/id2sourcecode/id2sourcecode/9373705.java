    public String decrypt(String str) throws Exception {
        try {
            byte[] dec = ZBase64Util.decode(str);
            byte[] all = dcipher.doFinal(dec);
            byte[] utf8 = new byte[all.length - 16];
            byte[] digest = new byte[16];
            System.arraycopy(all, 0, utf8, 0, utf8.length);
            System.arraycopy(all, utf8.length, digest, 0, digest.length);
            byte[] digest1 = md.digest(utf8);
            if (!Arrays.equals(digest, digest1)) {
                throw new Exception("MD5 hash does not match");
            }
            String ret = new String(utf8, "UTF8");
            return ret;
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }
    }
