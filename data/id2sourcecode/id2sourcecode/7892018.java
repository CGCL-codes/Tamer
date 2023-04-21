    private String getA1() {
        String Digest;
        MD.reset();
        Digest = user + ":" + realm + ":" + pass;
        MD.update(Digest.getBytes());
        Digest = HexString.convert(MD.digest(), 16);
        if (algor.toLowerCase().compareTo("md5-sess") != 0) return Digest;
        Digest += ":" + nonce + ":" + cnonce;
        MD.update(Digest.getBytes());
        Digest = HexString.convert(MD.digest(), 16);
        return Digest;
    }
