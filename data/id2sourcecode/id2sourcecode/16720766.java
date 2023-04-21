    public static byte[] digest(byte contents[][]) {
        CCNDigestHelper dh = new CCNDigestHelper();
        for (int i = 0; i < contents.length; ++i) {
            if (null != contents[i]) dh.update(contents[i], 0, contents[i].length);
        }
        return dh.digest();
    }
