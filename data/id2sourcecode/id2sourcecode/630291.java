    public synchronized void readLock() {
        Thread reader = Thread.currentThread();
        if (writeLocked()) {
            queue.addElement(reader);
            while (writeLocked() || reader != queue.firstElement()) {
                try {
                    Log.println("bloqueado", Log.LOCK);
                    this.wait();
                } catch (InterruptedException e) {
                    Log.println("Exception: " + e);
                }
            }
            Log.println("despertado, lector", Log.LOCK);
            queue.removeElement(reader);
        }
        Log.println("conseguido lock, lector", Log.LOCK);
        readers.addElement(reader);
        this.notifyAll();
    }
