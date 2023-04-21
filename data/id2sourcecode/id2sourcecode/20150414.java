    void doFinal(byte[] srcData, int srcOff, int srcLen, byte[] dstData, int dstOff) {
        if (srcLen != 0) {
            md5.update(srcData, srcOff, srcLen);
        }
        try {
            md5.digest(dstData, dstOff, dstData.length - dstOff);
        } catch (DigestException e) {
            throw new RuntimeException("output buffer too short");
        }
    }
