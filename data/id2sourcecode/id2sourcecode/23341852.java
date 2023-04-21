    public void initialize(final KerberosPrincipal principal) throws KrbCredCacheException {
        RandomAccessFile raf;
        FileLock lock;
        if (principal == null) {
            throw new KrbCredCacheException("null argument");
        }
        destroy();
        try {
            raf = new RandomAccessFile(this.file, "rw");
            lock = raf.getChannel().lock();
        } catch (Exception e) {
            throw new KrbCredCacheException("cannot open credentials file: " + this.file, e);
        }
        try {
            raf.writeShort(VERSION);
        } catch (IOException ie) {
            throw new KrbCredCacheException("cannot write version to credentials file: " + this.file, ie);
        }
        try {
            storePrincipal(raf, principal);
        } catch (KrbCredCacheException ke) {
            throw new KrbCredCacheException(ke);
        }
        try {
            lock.release();
            raf.getChannel().close();
        } catch (Exception e) {
            throw new KrbCredCacheException("cannot close credentials file: " + this.file, e);
        }
        return;
    }
