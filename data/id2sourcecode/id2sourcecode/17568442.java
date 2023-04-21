    @Override
    public String getLockID() {
        ensureOpen();
        String dirName = directory.getName().getURI();
        byte digest[];
        synchronized (DIGESTER) {
            digest = DIGESTER.digest(dirName.getBytes());
        }
        StringBuilder buf = new StringBuilder();
        buf.append("lucene-");
        for (int i = 0; i < digest.length; i++) {
            int b = digest[i];
            buf.append(HEX_DIGITS[(b >> 4) & 0xf]);
            buf.append(HEX_DIGITS[b & 0xf]);
        }
        return buf.toString();
    }
