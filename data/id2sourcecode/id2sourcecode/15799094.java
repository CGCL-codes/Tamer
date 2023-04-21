    public synchronized int getProgrammesCount() {
        int result = 0;
        Iterator it = getChannelsIterator();
        while (it.hasNext()) {
            TVChannel ch = (TVChannel) it.next();
            result += ch.getProgrammesCount();
        }
        return result;
    }
