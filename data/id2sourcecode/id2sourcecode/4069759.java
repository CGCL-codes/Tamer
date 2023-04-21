    private void verifyItemLogValidity() {
        int counter = 0;
        synchronized (itemLog) {
            Iterator<ItemLogEntry> itemLogIter = itemLog.iterator();
            while (itemLogIter.hasNext() && counter < itemMax) {
                counter++;
                ItemLogEntry next = itemLogIter.next();
                String msgHdr = next.theItem.getChannel() + ":" + next.theKey;
                final boolean diskNotNet = next.recordCounter == 0 && next.verifyCounter > 0;
                if (diskNotNet) {
                    assertTrue("we saw an item from disk which we didn't see from the net: " + msgHdr, !diskNotNet);
                }
                final boolean netNotDisk = next.recordCounter > 0 && next.verifyCounter == 0;
                if (netNotDisk) {
                    assertTrue("we saw an item from the Net which we didn't see from disk: " + msgHdr, !netNotDisk);
                }
            }
        }
    }
