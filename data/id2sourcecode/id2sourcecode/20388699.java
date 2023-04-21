    private void writeThread() {
        writeThread = new Thread() {

            public void run() {
                while (runFlag) {
                    try {
                        synchronized (monitorLock) {
                            monitorLock.wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        this.interrupted();
                    }
                    while (outQueue.hasRemaining()) {
                        ORPGMessage m = outQueue.pullMessage();
                        try {
                            connection.write(m.asNetworkMessage());
                            connection.flushOutboundBuffer();
                        } catch (IOException e) {
                            System.out.println("Exception in monitorThread.writeThread write error " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
                System.out.println("[DEBUG] NetworkClient - Monitor thread terminating");
            }
        };
        writeThread.start();
    }
