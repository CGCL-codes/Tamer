    private byte[] getBytesInternal(int numBytes) {
        MessageDigest sha1 = null;
        try {
            sha1 = MessageDigest.getInstance("SHA1");
        } catch (Exception e) {
            throw new Error("Error in RandomSeed, no sha1 hash");
        }
        int curLen = 0;
        byte[] bytes = new byte[numBytes];
        int offset = entropyRIdx;
        while (curLen < numBytes) {
            sha1.update((byte) System.currentTimeMillis());
            sha1.update(entropyPool, offset, 40);
            sha1.update((byte) evtCnt);
            byte[] material = sha1.digest();
            System.arraycopy(material, 0, bytes, curLen, ((numBytes - curLen > material.length) ? material.length : (numBytes - curLen)));
            curLen += material.length;
            offset += 40;
            offset %= entropyPool.length;
        }
        entropyRIdx = offset;
        entropyCount -= (numBytes * 8);
        if (entropyCount < 0) {
            entropyCount = 0;
        }
        return bytes;
    }
