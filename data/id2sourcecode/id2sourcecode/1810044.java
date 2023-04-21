    public void wake() {
        QueueProcessorThread obj = writeThread;
        if (obj != null) {
            synchronized (obj) {
                obj.notify();
            }
        }
    }
