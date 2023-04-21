    public static byte[][] makeContent(int numNodes, int nodeLength, boolean digest) {
        byte[][] bufs = new byte[numNodes][];
        byte[] tmpbuf = null;
        if (digest) tmpbuf = new byte[nodeLength];
        int blocklen = (digest ? CCNDigestHelper.DEFAULT_DIGEST_LENGTH : nodeLength);
        for (int i = 0; i < numNodes; ++i) {
            bufs[i] = new byte[blocklen];
            if (digest) {
                _rand.nextBytes(tmpbuf);
                bufs[i] = CCNDigestHelper.digest(tmpbuf);
            } else {
                _rand.nextBytes(bufs[i]);
            }
        }
        return bufs;
    }
