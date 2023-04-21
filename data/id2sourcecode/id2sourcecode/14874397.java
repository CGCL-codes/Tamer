                    public void run() {
                        try {
                            int read = -1;
                            while ((read = incomingInputStream.read()) != -1) {
                                outgoingOutputStream.write(read);
                            }
                        } catch (IOException e) {
                        }
                        LOG.info("write thread terminated");
                        synchronized (_runningThreads) {
                            _runningThreads.remove(this);
                        }
                    }
