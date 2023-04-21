        Writer() {
            writerThread = new Thread(this);
            writerThread.start();
        }
