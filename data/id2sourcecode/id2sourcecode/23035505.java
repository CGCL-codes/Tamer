    public static byte[] digest(byte[][] contents) {
        DigestHelper dh = new DigestHelper();
        for (int i = 0; i < contents.length; ++i) {
            dh.update(contents[i], 0, contents[i].length);
        }
        return dh.digest();
    }
