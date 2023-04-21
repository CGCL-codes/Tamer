                    public void run() {
                        try {
                            int read = -1;
                            while ((read = outgoingInputStream.read()) != -1) {
                                incomingOutputStream.write(read);
                            }
                        } catch (IOException e) {
                        }
                        LOG.info("read thread terminated");
                        synchronized (_runningThreads) {
                            _runningThreads.remove(this);
                        }
                    }
