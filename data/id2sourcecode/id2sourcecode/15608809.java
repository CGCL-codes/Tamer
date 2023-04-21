    protected Object clone() throws CloneNotSupportedException {
        Runtime r = (Runtime) super.clone();
        r._byteBuf = null;
        r.startTime = 0;
        r.fds = new FD[OPEN_MAX];
        for (int i = 0; i < OPEN_MAX; i++) if (fds[i] != null) r.fds[i] = fds[i].dup();
        int totalPages = writePages.length;
        r.readPages = new int[totalPages][];
        r.writePages = new int[totalPages][];
        for (int i = 0; i < totalPages; i++) {
            if (readPages[i] == null) continue;
            if (writePages[i] == null) r.readPages[i] = readPages[i]; else r.readPages[i] = r.writePages[i] = (int[]) writePages[i].clone();
        }
        return r;
    }
