        public void run() {
            byte[] cbuf = new byte[BUFSZ];
            thread = Thread.currentThread();
            int readLength = 0;
            try {
                while (running && (readLength = reader.read(cbuf, 0, cbuf.length)) != -1) {
                    while (isPaused()) {
                        log.info("Channel is paused. \"wait\"ing...");
                        synchronized (this) {
                            try {
                                wait();
                            } catch (InterruptedException e) {
                            }
                        }
                    }
                    bytes += readLength;
                    con.setCurrentChannel(this);
                    if (debugBinary) printBinaryBuf(cbuf, readLength);
                    if (debugText) printTextBuf(cbuf, readLength);
                    writer.write(cbuf, 0, readLength);
                    writer.flush();
                }
            } catch (IOException e) {
                final String msg = e.getMessage();
                if (msg != null && !msg.startsWith("Socket closed")) log.error("Exception running proxy: " + msg);
                running = false;
            }
            try {
                out.close();
            } catch (IOException ioex) {
                println("Channel.run() could not close socket");
            }
            con.channelClosed(this);
        }
