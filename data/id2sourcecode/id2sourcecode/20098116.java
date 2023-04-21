    public Hash calculateHash(int blockNumber) throws IOException {
        byte buf[] = new byte[getBlockSize(blockNumber)];
        raf.seek(getOffset(blockNumber));
        int r = raf.read(buf);
        if (T.t) T.ass(r == buf.length, "wtf");
        Tiger t = new Tiger();
        t.update(buf);
        return new Hash(t.digest());
    }
