    public synchronized void normalizeTime() {
        timeCalculated = false;
        Iterator it = getChannelsIterator();
        while (it.hasNext()) {
            TVChannel ch = (TVChannel) it.next();
            ch.normalizeTime();
        }
    }
