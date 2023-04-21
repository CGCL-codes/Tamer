    private static byte[] produceParentBytes(MerkleTree mt, byte[] lBytes, byte[] rBytes) {
        boolean lChildNull = false;
        boolean rChildNull = false;
        byte[] retBytes = null;
        if (lBytes.length == 1 && lBytes[0] == 0) lChildNull = true;
        if (rBytes.length == 1 && rBytes[0] == 0) rChildNull = true;
        if (!lChildNull && !rChildNull) {
            mt.resetDigest();
            mt.updateDigest(lBytes);
            mt.updateDigest(rBytes);
            retBytes = mt.digest();
        } else if (!lChildNull && rChildNull) {
            retBytes = LtansUtils.duplicateByteArray(lBytes);
        } else if (lChildNull && !rChildNull) {
            retBytes = LtansUtils.duplicateByteArray(rBytes);
        } else if (lChildNull && rChildNull) {
            retBytes = new byte[1];
            retBytes[0] = 0;
        }
        return retBytes;
    }
