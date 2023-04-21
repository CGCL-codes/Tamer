    private byte[] getKey(byte index) {
        if (index <= 1) return __key;
        byte[] buff = new byte[__kij.length + SHA1Digest.HASH_SIZE + KEY_INDEX_LENGTH];
        byte[] hash = null;
        int i = 2;
        SHA1Digest md = new SHA1Digest();
        hash = __key;
        while (i <= index) {
            System.arraycopy(__kij, 0, buff, 0, __kij.length);
            System.arraycopy(hash, 0, buff, __kij.length, SHA1Digest.HASH_SIZE);
            buff[buff.length - 1] = (byte) i;
            hash = md.digest(buff);
            i++;
        }
        return hash;
    }
