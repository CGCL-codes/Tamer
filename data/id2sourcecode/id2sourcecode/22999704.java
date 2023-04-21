    public void sort() {
        long startime = System.currentTimeMillis();
        synchronized (this) {
            if (!needSort()) return;
            if (logger.isDebugEnabled()) logger.debug("Groupkey write start!Groupkey status：" + toString() + " by Thread:" + Thread.currentThread().getName());
            for (Topkey key : sortedTopkeys) distinctTopkeys.put(key.getKey(), key);
            distinctTopkeys.putAll(sampledTopkeys);
            reset();
        }
        List<Topkey> sortedArr = new ArrayList<Topkey>(distinctTopkeys.values());
        distinctTopkeys.clear();
        Collections.sort(sortedArr);
        if (sortedArr.size() > size) {
            sortedArr.subList(size, sortedArr.size()).clear();
        }
        this.sortedTopkeys = sortedArr;
        if (GkcConfig.getInstance().isStore() && sortedTopkeys.size() > 0) cachePersistent();
        long timeEclipse = System.currentTimeMillis() - startime;
        if (timeEclipse > 1000) logger.warn("Groupkey write time too long! timeElapse:" + timeEclipse + " ms KeyBuffer.size:" + KeyBuffer.size());
        if (logger.isDebugEnabled()) logger.debug("Groupkey status:" + toString() + "sortedTopkeys:" + descSortedTopkeys());
    }
