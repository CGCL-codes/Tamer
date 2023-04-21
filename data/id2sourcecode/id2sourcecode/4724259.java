    public void doFinal(byte[] in_buff, short in_off, short in_len, byte[] out_buff, short out_off) {
        digest.update(in_buff, in_off, in_len);
        byte[] hash = digest.digest();
        System.arraycopy(hash, 0, out_buff, out_off, hash.length);
        return;
    }
