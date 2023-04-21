    public void update(byte b) {
        md4.update(b);
        length++;
        if ((length % BLOCKSIZE) == 0) {
            System.arraycopy(md4.digest(), 0, edonkeyHash, 0, 16);
            md4final.update(edonkeyHash, 0, 16);
            md4.reset();
        }
    }
