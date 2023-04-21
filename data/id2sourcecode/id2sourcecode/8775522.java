    public void load(long pos, int nBytes) {
        if (nBytes <= 0) return;
        List<CacheEntry> l = new ArrayList();
        long stopIdx = pos + nBytes;
        try {
            synchronized (cache) {
                for (; pos < stopIdx; pos += bufsize) {
                    long idx = pos / bufsize;
                    if (cache.containsKey(idx)) continue;
                    if (nOutstanding >= nReadAhead) return;
                    long end = 0;
                    long start = idx * bufsize;
                    try {
                        end = SMath.min(length(), start + bufsize);
                    } catch (IOException ex) {
                    }
                    CacheEntry ce = new CacheEntry(idx, start, end);
                    ce.isBusy = true;
                    cache.put(idx, ce);
                    l.add(ce);
                }
            }
        } finally {
            for (CacheEntry ce : l) {
                try {
                    new FillCacheEntry((HttpURLConnection) url.openConnection(), ce).start();
                } catch (IOException ex) {
                    synchronized (cache) {
                        cache.remove(ce.idx);
                    }
                }
            }
        }
    }
