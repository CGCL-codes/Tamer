    public CHK(Storables storables, int log2size) throws KeyException {
        super(20, log2size, keyNumber);
        if (!storables.isLegalForCHK()) throw new KeyException("illegal Storables");
        Digest ctx = SHA1.getInstance();
        storables.hashUpdate(ctx);
        System.arraycopy(ctx.digest(), 0, val, 0, 20);
    }
