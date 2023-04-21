    public final void getMac(byte[] out, int off) {
        mac.digest(out, off);
    }
