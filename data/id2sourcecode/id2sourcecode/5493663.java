        public void run() {
            writerThread = Thread.currentThread();
            while (!writerThread.isInterrupted()) {
                if (queue.isEmpty()) {
                    try {
                        synchronized (queue) {
                            queue.wait(1000 * 30);
                        }
                    } catch (InterruptedException ex) {
                        return;
                    }
                } else {
                    Calculable calc = (Calculable) queue.remove(0);
                    addToHistory(calc);
                    if (archivable != null) archivable.archive(calc);
                }
            }
        }
