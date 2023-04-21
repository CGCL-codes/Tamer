        @Override
        public int read() throws IOException {
            int result;
            synchronized (buffer) {
                if (writePos == readPos) {
                    try {
                        buffer.wait();
                    } catch (InterruptedException ex) {
                        return -1;
                    }
                }
                result = buffer[readPos++];
                if (readPos == buffer.length) {
                    readPos = 0;
                    log.info("Buffer read wrap");
                }
            }
            return result;
        }
