    private void setBytes(byte[] bytes, int nbBytes) {
        nbUnusedBits = bytes[0] & 0x07;
        nbBits = (nbBytes * 8) - nbUnusedBits;
        for (int i = 0; i < nbBytes; i++) {
            this.bytes[i] = bytes[i + 1];
        }
    }
