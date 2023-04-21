    public long setUp() throws IOException {
        RandomAccessFile kRaf = new RandomAccessFile(path + ".keys", "rw");
        kRaf.setLength(size * FREE.length);
        RandomAccessFile vRaf = new RandomAccessFile(path + ".pos", "rw");
        vRaf.setLength(size * 8);
        tRaf = new RandomAccessFile(path + ".ctimes", "rw");
        tRaf.setLength(size * 8);
        this.kFC = kRaf.getChannel();
        this.vFC = vRaf.getChannel();
        RandomAccessFile _bpos = new RandomAccessFile(path + ".bpos", "rw");
        _bpos.setLength(8);
        bgst = _bpos.readLong();
        if (bgst < 0) {
            SDFSLogger.getLog().info("Hashtable " + path + " did not close correctly. scanning ");
            bgst = this.getBigestKey();
        }
        _bpos.seek(0);
        _bpos.writeLong(-1);
        _bpos.close();
        claims = ByteBuffer.allocateDirect(size);
        return bgst;
    }
