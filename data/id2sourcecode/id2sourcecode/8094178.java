    public String getHexEncodedMD5() {
        String value = null;
        if (md5 != null) {
            value = new String(Hex.encodeHex(md5.digest()));
        }
        return value;
    }
