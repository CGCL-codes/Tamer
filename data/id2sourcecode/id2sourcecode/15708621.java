    public String getFilename(String password, int index) throws IOException {
        MessageDigest sha1;
        try {
            sha1 = MessageDigest.getInstance("SHA1", cryptProvider);
        } catch (GeneralSecurityException ex) {
            throw new IOException(ex.toString());
        }
        byte[] digest = sha1.digest((password + "-" + index).getBytes("UTF-8"));
        StringBuffer sb = new StringBuffer(digest.length * 2);
        for (int i = 0; i < digest.length; i++) {
            int b = digest[i] & 0xff;
            sb.append(b < 16 ? "0" : "").append(Integer.toHexString(b));
        }
        return sb.toString() + ".jpds";
    }
