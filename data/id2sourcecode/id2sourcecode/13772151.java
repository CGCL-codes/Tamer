    public Object nextForWriting() {
        Object ret = null;
        boolean unpause = false;
        synchronized (readyForWriting) {
            ret = (readyForWriting.size() > 0) ? readyForWriting.remove(0) : null;
            if (noWritesForYou && readyForWriting.size() < writeRestart) {
                unpause = true;
            }
        }
        if (unpause) {
            synchronized (writeBlocker) {
                noWritesForYou = false;
                writeBlocker.notifyAll();
            }
        }
        return (ret);
    }
