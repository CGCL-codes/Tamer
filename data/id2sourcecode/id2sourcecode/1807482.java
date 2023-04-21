    public synchronized void removeThreadsSet(LinksThread thread) {
        threadsSet.remove(thread);
        removeThread(thread);
        if (mapSites.size() < 2) {
            SimpleLog.getInstance().writeLog(7, "numero de threads: " + threadsSet.size());
            SimpleLog.getInstance().writeLog(7, "numero de numero de sites: " + mapSites.size());
        }
        if (threadsSet.size() == 1) {
            Iterator<LinksThread> threadIterator = threadsSet.iterator();
            if (threadIterator.hasNext()) {
                LinksThread threadAux = threadIterator.next();
                SimpleLog.getInstance().writeLog(7, "último sitio: " + threadAux.getKeySite());
            }
        }
    }
