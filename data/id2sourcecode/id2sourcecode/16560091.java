        public JitterStream(AudioInputStream s, int buffersize, int smallbuffersize) {
            this.w_count = 10 * (buffersize / smallbuffersize);
            if (w_count < 100) w_count = 100;
            this.buffers = new byte[(buffersize / smallbuffersize) + 10][smallbuffersize];
            this.bbuffer_max = MAX_BUFFER_SIZE / smallbuffersize;
            this.stream = s;
            Runnable runnable = new Runnable() {

                public void run() {
                    AudioFormat format = stream.getFormat();
                    int bufflen = buffers[0].length;
                    int frames = bufflen / format.getFrameSize();
                    long nanos = (long) (frames * 1000000000.0 / format.getSampleRate());
                    long now = System.nanoTime();
                    long next = now + nanos;
                    int correction = 0;
                    while (true) {
                        synchronized (JitterStream.this) {
                            if (!active) break;
                        }
                        int curbuffsize;
                        synchronized (buffers) {
                            curbuffsize = writepos - readpos;
                            if (correction == 0) {
                                w++;
                                if (w_min != Integer.MAX_VALUE) {
                                    if (w == w_count) {
                                        correction = 0;
                                        if (w_min < w_min_tol) {
                                            correction = (w_min_tol + w_max_tol) / 2 - w_min;
                                        }
                                        if (w_min > w_max_tol) {
                                            correction = (w_min_tol + w_max_tol) / 2 - w_min;
                                        }
                                        w = 0;
                                        w_min = Integer.MAX_VALUE;
                                    }
                                }
                            }
                        }
                        while (curbuffsize > bbuffer_max) {
                            synchronized (buffers) {
                                curbuffsize = writepos - readpos;
                            }
                            synchronized (JitterStream.this) {
                                if (!active) break;
                            }
                            try {
                                Thread.sleep(1);
                            } catch (InterruptedException e) {
                            }
                        }
                        if (correction < 0) correction++; else {
                            byte[] buff = nextWriteBuffer();
                            try {
                                int n = 0;
                                while (n != buff.length) {
                                    int s = stream.read(buff, n, buff.length - n);
                                    if (s < 0) throw new EOFException();
                                    if (s == 0) Thread.yield();
                                    n += s;
                                }
                            } catch (IOException e1) {
                            }
                            commit();
                        }
                        if (correction > 0) {
                            correction--;
                            next = System.nanoTime() + nanos;
                            continue;
                        }
                        long wait = next - System.nanoTime();
                        if (wait > 0) {
                            try {
                                Thread.sleep(wait / 1000000L);
                            } catch (InterruptedException e) {
                            }
                        }
                        next += nanos;
                    }
                }
            };
            thread = new Thread(runnable);
            thread.setDaemon(true);
            thread.setPriority(Thread.MAX_PRIORITY);
            thread.start();
        }
