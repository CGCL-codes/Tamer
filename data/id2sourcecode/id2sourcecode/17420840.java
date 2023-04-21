    public KeyMaterialGenerator(byte[] kij, byte[] i, byte[] j, byte[] hitI, byte[] hitR) {
        byte[] lhs;
        byte[] rhs;
        int offset = 0;
        byte[] buff = new byte[kij.length + i.length + j.length + hitI.length + hitR.length + KEY_INDEX_LENGTH];
        if (__sort(hitI, hitR) < 0) {
            lhs = hitI;
            rhs = hitR;
        } else {
            lhs = hitR;
            rhs = hitI;
        }
        __kij = new byte[kij.length];
        System.arraycopy(kij, 0, __kij, 0, kij.length);
        System.arraycopy(kij, 0, buff, offset, kij.length);
        offset += kij.length;
        System.arraycopy(lhs, 0, buff, offset, lhs.length);
        offset += lhs.length;
        System.arraycopy(rhs, 0, buff, offset, rhs.length);
        offset += rhs.length;
        System.arraycopy(i, 0, buff, offset, i.length);
        offset += i.length;
        System.arraycopy(j, 0, buff, offset, j.length);
        offset += j.length;
        buff[offset] = 0x1;
        SHA1Digest md = new SHA1Digest();
        __key = md.digest(buff);
        for (int k = 0; k < KEYS; k++) {
            byte[] key = getKey((byte) (k + 1));
            System.arraycopy(key, 0, __keyMat, k * SHA1Digest.HASH_SIZE, SHA1Digest.HASH_SIZE);
        }
    }
