        @Override
        public void run() {
            try {
                logger.fine("getMinimumTransferSize: " + sourceStream.getMinimumTransferSize());
                final byte[] buffer = new byte[sourceStream.getMinimumTransferSize() > DEFAULT_BUFFER_SIZE ? sourceStream.getMinimumTransferSize() : DEFAULT_BUFFER_SIZE];
                boolean eos = false;
                while (!isClosing() && !eos) {
                    if (USE_TRANSFER_HANDLER) {
                        synchronized (dataAvailable) {
                            dataAvailable.waitUntil(true);
                            dataAvailable.setValue(false);
                        }
                    }
                    while (true) {
                        int read = sourceStream.read(buffer, 0, buffer.length);
                        if (read == 0) {
                            break;
                        } else if (read < 0) {
                            eos = true;
                            os.close();
                            logger.fine("EOS");
                            notifyDataSinkListeners(new EndOfStreamEvent(StreamDataSink.this, "EOS"));
                            break;
                        } else {
                            os.write(buffer, 0, read);
                        }
                    }
                }
                if (!eos) logger.warning("Closed before EOS");
            } catch (IOException e) {
                logger.log(Level.WARNING, "" + e, e);
                notifyDataSinkListeners(new DataSinkErrorEvent(StreamDataSink.this, e.getMessage()));
            } catch (InterruptedException e) {
                logger.log(Level.WARNING, "" + e, e);
            } finally {
                setClosed();
            }
        }
