    protected ChecksumPair generateSum(byte[] buf, int off, int len) {
        ChecksumPair p = new ChecksumPair();
        config.weakSum.check(buf, off, len);
        config.strongSum.update(buf, off, len);
        if (config.checksumSeed != null) {
            config.strongSum.update(config.checksumSeed, 0, config.checksumSeed.length);
        }
        p.weak = config.weakSum.getValue();
        p.strong = new byte[config.strongSumLength];
        System.arraycopy(config.strongSum.digest(), 0, p.strong, 0, config.strongSumLength);
        p.offset = count;
        p.length = len;
        count += len;
        return p;
    }
