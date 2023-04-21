        public void commit() {
            synchronized (buffers_mutex) {
                writepos++;
                if ((writepos - readpos) > buffers.length) {
                    int newsize = (writepos - readpos) + 10;
                    newsize = Math.max(buffers.length * 2, newsize);
                    buffers = new byte[newsize][buffers[0].length];
                }
            }
        }
