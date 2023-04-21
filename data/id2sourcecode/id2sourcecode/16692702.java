    public void doFinal(byte[] buf, int offset) {
        byte[] result = md.digest();
        md.update(k_opad, 0, B);
        md.update(result, 0, bsize);
        try {
            md.digest(buf, offset, bsize);
        } catch (Exception e) {
        }
        md.update(k_ipad, 0, B);
    }
