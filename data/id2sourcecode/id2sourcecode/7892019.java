    private String getA2() {
        String Digest;
        MD.reset();
        Digest = method + ":" + file;
        MD.update(Digest.getBytes());
        Digest = HexString.convert(MD.digest(), 16);
        return Digest;
    }
