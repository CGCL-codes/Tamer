    public void run() {
        if (Debug.TRACE) {
            Debug.println("BufferedSocketOutputStream.run(): begin");
        }
        running = true;
        try {
            while (!closed || queue.availableBuffers() > 0) {
                if (queue.availableBuffers() < 1) {
                    synchronized (this) {
                        try {
                            wait(100);
                        } catch (InterruptedException e) {
                        }
                    }
                    continue;
                }
                int readBytes = queue.removeRead(buffer, 0, buffer.length);
                try {
                    m_stream.write(buffer, 0, readBytes);
                    pos += readBytes;
                } catch (IOException ioe) {
                    if (Debug.DEBUG) {
                        Debug.println("BufferedSocketOutputStream.run(): stream is closed -> break");
                    }
                    break;
                }
            }
        } catch (Throwable t) {
            if (Debug.ERROR) {
                Debug.println("BufferedSocketOutputStream.run():");
                Debug.println(t);
            }
        }
        closeStream();
        if (Debug.DEBUG) {
            Debug.println("BufferedSocketOutputStream.run(): smooth thread death. Wrote " + pos + " bytes.");
        }
        running = false;
    }
