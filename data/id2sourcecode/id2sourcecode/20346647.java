        public void stop() {
            running = false;
            writerThread.interrupt();
        }
