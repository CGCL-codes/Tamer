    public synchronized void writeLock() {
        Thread writer = Thread.currentThread();
        if (writeLocked() || readLocked()) {
            queue.addElement(writer);
            while (writeLocked() || readLocked() || writer != queue.firstElement()) {
                try {
                    Log.println("bloqueado", Log.LOCK);
                    this.wait();
                } catch (InterruptedException e) {
                    Log.println("Exception: " + e);
                }
            }
            Log.println("despertado, escritor", Log.LOCK);
            queue.removeElement(writer);
        }
        Log.println("conseguido lock, escritor", Log.LOCK);
        currentWriter = writer;
        this.notifyAll();
    }
