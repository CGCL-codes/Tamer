        public void run() {
            byte[] buffer = new byte[getMaximumPacketSize()];
            ByteArrayWriter baw = new ByteArrayWriter();
            try {
                socket.setSoTimeout(2000);
            } catch (SocketException ex2) {
            }
            try {
                int read = 0;
                while ((read >= 0) && !isClosed()) {
                    try {
                        read = socket.getInputStream().read(buffer);
                    } catch (InterruptedIOException ex1) {
                        read = ex1.bytesTransferred;
                    }
                    synchronized (state) {
                        if (isClosed() || isLocalEOF()) {
                            break;
                        }
                        if (read > 0) {
                            baw.write(buffer, 0, read);
                            sendChannelData(baw.toByteArray());
                            baw.reset();
                        }
                    }
                }
            } catch (IOException ex) {
            }
            try {
                synchronized (state) {
                    if (!isLocalEOF()) {
                        setLocalEOF();
                    }
                    if (isOpen()) {
                        close();
                    }
                }
            } catch (Exception ex) {
                log.info("Failed to send channel EOF message: " + ex.getMessage());
            }
            thread = null;
        }
