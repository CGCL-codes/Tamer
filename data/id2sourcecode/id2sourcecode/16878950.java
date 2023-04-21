    public String getMd5Hash() throws NoSuchAlgorithmException {
        byte[] certBytes = this.getDocument();
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] md5hash = new byte[32];
        md.update(certBytes, 0, certBytes.length);
        md5hash = md.digest();
        return encodeHexString(md5hash);
    }
