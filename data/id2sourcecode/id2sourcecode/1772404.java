    private String shaDigestForFile(String filename) throws IOException {
        DigestInputStream str = digestInputStream(inputStreamForFile(filename));
        byte[] dummy = new byte[4096];
        for (int i = 1; i > 0; i = str.read(dummy)) ;
        str.close();
        return HexBin.bytesToString(str.getMessageDigest().digest());
    }
