    private void mgf1(byte[] seed, int seedOfs, int seedLen, byte[] out, int outOfs, int maskLen) throws BadPaddingException {
        byte[] C = new byte[4];
        byte[] digest = new byte[20];
        while (maskLen > 0) {
            mgfMd.update(seed, seedOfs, seedLen);
            mgfMd.update(C);
            try {
                mgfMd.digest(digest, 0, digest.length);
            } catch (DigestException e) {
                throw new BadPaddingException(e.toString());
            }
            for (int i = 0; (i < digest.length) && (maskLen > 0); maskLen--) {
                out[outOfs++] ^= digest[i++];
            }
            if (maskLen > 0) {
                for (int i = C.length - 1; (++C[i] == 0) && (i > 0); i--) {
                }
            }
        }
    }
