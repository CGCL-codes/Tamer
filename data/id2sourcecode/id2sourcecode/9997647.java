    final synchronized void writeLock() {
        lockRequested++;
        writeRequested++;
        while ((readCount > 0) || (write == true)) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        write = true;
        lockRequested--;
        writeRequested--;
    }
