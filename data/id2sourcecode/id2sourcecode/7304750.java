    public byte[] generateStrongSum(byte[] buf, int off, int len) {
        config.strongSum.update(buf, off, len);
        byte[] strongSum = new byte[config.strongSumLength];
        System.arraycopy(config.strongSum.digest(), 0, strongSum, 0, strongSum.length);
        return strongSum;
    }
