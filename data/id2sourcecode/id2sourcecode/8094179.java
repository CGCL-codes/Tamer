    public String getHexEncodedSHA1() {
        String value = null;
        if (sha1 != null) {
            value = new String(Hex.encodeHex(sha1.digest()));
        }
        return value;
    }
