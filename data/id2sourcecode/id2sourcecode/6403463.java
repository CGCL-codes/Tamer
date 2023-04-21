    private void processQueue() {
        for (int i = 0; i < threadedIOQueue.size(); i++) {
            IThreadedFileIO ithreadedfileio = (IThreadedFileIO) threadedIOQueue.get(i);
            boolean flag = ithreadedfileio.writeNextIO();
            if (!flag) {
                threadedIOQueue.remove(i--);
                savedIOCounter++;
            }
            try {
                if (!isThreadWaiting) {
                    Thread.sleep(10L);
                } else {
                    Thread.sleep(0L);
                }
            } catch (InterruptedException interruptedexception1) {
                interruptedexception1.printStackTrace();
            }
        }
        if (threadedIOQueue.isEmpty()) {
            try {
                Thread.sleep(25L);
            } catch (InterruptedException interruptedexception) {
                interruptedexception.printStackTrace();
            }
        }
    }
