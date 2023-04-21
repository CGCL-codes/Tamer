        public void run() {
            while (keepGoing) {
                boolean foundOne = false;
                ItemLogEntry[] copyItemLog = itemLog.toArray(new ItemLogEntry[0]);
                for (int i = 0; i < copyItemLog.length; i++) {
                    ItemLogEntry entry = copyItemLog[i];
                    synchronized (entry) {
                        if (!entry.deleted) {
                            PersistChanGrpMgr theGrp = managers[entry.getPersistChanGrpMgrIdx()];
                            Channel theChan = (Channel) entry.theItem.getChannel();
                            theGrp.deActivate();
                            int before = theGrp.getItemCount(theChan);
                            int after = theGrp.deleteItemFromChannel(theChan, entry.theItem);
                            if (getActiveSemaphor()) theGrp.activate();
                            logger.debug("Deleted. Count before =" + before + " /after: " + after);
                            assertEquals("Not the rigth number of items", before, after + 1);
                            entry.deleted = true;
                            foundOne = true;
                        }
                    }
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
                if (!foundOne) keepGoing = false;
            }
            synchronized (this) {
                notifyAll();
            }
        }
