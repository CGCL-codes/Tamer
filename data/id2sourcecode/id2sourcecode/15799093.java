    public synchronized void moveFrom(final TVData other) {
        timeCalculated = false;
        Iterator it = other.getChannelsIterator();
        while (it.hasNext()) {
            TVChannel ch = (TVChannel) it.next();
            TVChannel myCh = get(ch.getID());
            myCh.moveFrom(ch);
        }
    }
