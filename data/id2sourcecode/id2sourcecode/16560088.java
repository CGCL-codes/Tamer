        public byte[] nextReadBuffer() {
            synchronized (buffers_mutex) {
                if (writepos > readpos) {
                    int w_m = writepos - readpos;
                    if (w_m < w_min) w_min = w_m;
                    int buffpos = readpos;
                    readpos++;
                    return buffers[buffpos % buffers.length];
                }
                w_min = -1;
                w = w_count - 1;
            }
            while (true) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    return null;
                }
                synchronized (buffers_mutex) {
                    if (writepos > readpos) {
                        w = 0;
                        w_min = -1;
                        w = w_count - 1;
                        int buffpos = readpos;
                        readpos++;
                        return buffers[buffpos % buffers.length];
                    }
                }
            }
        }
