    public void run() {
        try {
            int in = iStream.read();
            while (in != -1) {
                store[writeCursor % 5000] = in;
                writeCursor++;
                if ((writeCursor - readCursor) >= 5000) {
                    int count = 0;
                    while ((writeCursor - readCursor) >= 5000) {
                        try {
                            Thread.sleep(10);
                        } catch (Exception e) {
                        }
                        count++;
                        if (count > 1000) {
                            throw new IOException("HttpPostInputStreamBuffer.run timed out.");
                        }
                    }
                }
                in = iStream.read();
            }
        } catch (Exception e) {
            exception = e;
        }
        finished = true;
    }
