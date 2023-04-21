    public List<SyndEntryImpl> getChannel(int numEntries) {
        List<SyndEntryImpl> res = new ArrayList<SyndEntryImpl>();
        int sx = entries.size() - numEntries;
        if (sx < 0) {
            sx = 0;
        }
        synchronized (this) {
            res.addAll(entries.subList(sx, entries.size()));
        }
        return res;
    }
