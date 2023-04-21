        private void putByte(int b) {
            synchronized (buffer) {
                if (writePos == buffer.length - 1 && readPos == 0) {
                    throw new RuntimeException("Buffer full no read");
                } else {
                    if (writePos == readPos - 1) {
                        throw new RuntimeException("Buffer Overrun at: " + writePos);
                    }
                }
                buffer[writePos++] = b;
                if (writePos == buffer.length) {
                    writePos = 0;
                    log.info("Buffer write wrap");
                }
                buffer.notifyAll();
            }
        }
