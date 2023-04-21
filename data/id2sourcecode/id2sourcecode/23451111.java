    private StringBuffer getLockPrefix() {
        String dirName;
        try {
            dirName = directory.getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
        byte digest[];
        synchronized (DIGESTER) {
            digest = DIGESTER.digest(dirName.getBytes());
        }
        StringBuffer buf = new StringBuffer();
        buf.append("lucene-");
        for (int i = 0; i < digest.length; i++) {
            int b = digest[i];
            buf.append(HEX_DIGITS[(b >> 4) & 0xf]);
            buf.append(HEX_DIGITS[b & 0xf]);
        }
        return buf;
    }
