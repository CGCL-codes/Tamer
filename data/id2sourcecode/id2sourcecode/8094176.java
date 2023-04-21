    public String getBase64EncodedMD5() {
        String value = null;
        if (md5 != null) {
            value = new String(Base64.encodeBase64(md5.digest()));
        }
        return value;
    }
