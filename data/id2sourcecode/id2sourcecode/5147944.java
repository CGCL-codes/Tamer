    public int doFinal(byte[] out, int outOff) {
        try {
            return md.digest(out, outOff, 20);
        } catch (DigestException ex) {
            return 0;
        }
    }
