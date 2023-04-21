    public synchronized void acquireExclusive() {
        try {
            logger.debug("Thread " + Thread.currentThread().getName() + " tries to acquire exclusive lock");
            int myPrio = prio + writerPrio;
            prio++;
            writer.put(new Integer(myPrio), Thread.currentThread());
            while ((currentWriter != null) || (readerCount > 0) || ((!reader.isEmpty()) && (prio >= ((Integer) reader.firstKey()).intValue()))) {
                wait();
            }
            writer.remove(new Integer(myPrio));
            currentWriter = Thread.currentThread();
            logger.debug("Thread " + Thread.currentThread().getName() + " acquired exclusive lock");
        } catch (InterruptedException e) {
        }
    }
