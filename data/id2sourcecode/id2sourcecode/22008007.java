    public void update(byte[] buffer, int offset, int len) {
        int zuSchreiben = len - offset;
        int passed = (int) (length % BLOCKSIZE);
        int platz = BLOCKSIZE - passed;
        if (platz > zuSchreiben) {
            md4.update(buffer, offset, len);
            length += len;
        } else if (platz == zuSchreiben) {
            md4.update(buffer, offset, len);
            length += len;
            System.arraycopy(md4.digest(), 0, edonkeyHash, 0, 16);
            md4final.update(edonkeyHash, 0, 16);
            md4.reset();
        } else if (platz < zuSchreiben) {
            md4.update(buffer, offset, platz);
            length += platz;
            System.arraycopy(md4.digest(), 0, edonkeyHash, 0, 16);
            md4final.update(edonkeyHash, 0, 16);
            md4.reset();
            md4.update(buffer, offset + platz, zuSchreiben - platz);
            length += zuSchreiben - platz;
        }
    }
