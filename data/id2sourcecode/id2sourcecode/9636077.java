    private byte[] getByteArray() {
        if (virgin) {
            digest = md.digest();
            virgin = false;
        }
        byte[] save = new byte[digest.length];
        System.arraycopy(digest, 0, save, 0, digest.length);
        return save;
    }
