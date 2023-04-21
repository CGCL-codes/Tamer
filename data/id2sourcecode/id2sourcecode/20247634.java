                    public void run() {
                        NIODispatcher.instance().register(getChannel(), AbstractNBSocket.this);
                        try {
                            observer.handleConnect(AbstractNBSocket.this);
                        } catch (IOException iox) {
                            NIODispatcher.instance().executeLaterAlways(new Runnable() {

                                public void run() {
                                    shutdown();
                                }
                            });
                        }
                    }
