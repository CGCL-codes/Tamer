    public String getBase64EncodedSHA1() {
        String value = null;
        if (sha1 != null) {
            value = new String(Base64.encodeBase64(sha1.digest()));
        }
        return value;
    }
