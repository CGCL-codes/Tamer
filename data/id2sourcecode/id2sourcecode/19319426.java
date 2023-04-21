                @Override
                public void run() {
                    try {
                        writerThread.finishSendingMessages();
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
