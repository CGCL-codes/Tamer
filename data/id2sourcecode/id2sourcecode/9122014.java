        @Override
        public void forward(ByteBuffer input) throws IOException {
            switch(inputHandling) {
                case FORWARD:
                    try {
                        ChannelWriter peerWriter = peer.getChannelWriter();
                        peerWriter.forward(input);
                        forwardDone = true;
                        lock.lock();
                        try {
                            forwarded.signal();
                        } finally {
                            lock.unlock();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    break;
                case ECHO:
                default:
                    lock.lock();
                    try {
                        channelWriter.forward(input);
                        echoDone = true;
                        enqueued.signal();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
            }
            try {
                Thread.sleep(1000100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
