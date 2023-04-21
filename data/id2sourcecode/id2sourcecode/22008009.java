    public byte[] getByteArray() {
        if (virgin) {
            if (length < BLOCKSIZE) {
                System.arraycopy(md4.digest(), 0, edonkeyHash, 0, 16);
            } else {
                IMessageDigest md4temp = (IMessageDigest) md4final.clone();
                md4temp.update(md4.digest(), 0, 16);
                System.arraycopy(md4temp.digest(), 0, edonkeyHash, 0, 16);
            }
            virgin = false;
            digest = edonkeyHash;
        }
        byte[] save = new byte[digest.length];
        System.arraycopy(digest, 0, save, 0, digest.length);
        return save;
    }
