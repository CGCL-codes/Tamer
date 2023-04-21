    public synchronized void clearProgrammes() {
        timeCalculated = false;
        Iterator it = getChannelsIterator();
        while (it.hasNext()) {
            TVChannel ch = (TVChannel) it.next();
            ch.clearProgrammes();
        }
    }
