    public void write(byte[] b, int off, int len) throws IOException {
        if (len <= bytesPerWindow) {
            out.write(b, off, len);
        } else {
            byte[] buffer = new byte[bytesPerWindow];
            ByteArrayInputStream in = new ByteArrayInputStream(b, off, len);
            while (in.available() > 0) {
                checkTime();
                int numread = in.read(buffer);
                out.write(buffer, 0, numread);
                bytesWrittenThisWindow += numread;
                if (bytesWrittenThisWindow > bytesPerWindow) {
                    synchronized (out) {
                        long howLongToWait = windowEnd - System.currentTimeMillis();
                        if (howLongToWait > 0) {
                            try {
                                out.wait(howLongToWait);
                            } catch (InterruptedException e) {
                                throw new IOException("Write interrupted");
                            }
                        }
                    }
                }
            }
        }
    }
