    public double connect(String name, int port, ConnectObserver connectObserver) {
        Log.logDebug("network", "NetworkService.connect", "Connecting to server on port " + port);
        Channel channel = null;
        try {
            socket = null;
            int i = 0;
            while ((socket == null) && (i < 10)) {
                for (int j = 0; j < 10; ++j) {
                    try {
                        Thread.sleep(100);
                        if (connectObserver != null) {
                            connectObserver.notice();
                        }
                    } catch (InterruptedException e) {
                    }
                }
                socket = new Socket(InetAddress.getByName(name), port);
                ++i;
            }
            if (socket == null) {
                return -1;
            }
            channel = new Channel(socket, this);
            addConnectedChannel(channel);
        } catch (Exception e) {
            Log.logError("network", "NetworkService.connect", "Error while connecting to " + name + ":" + port);
            return -1;
        }
        return channel.getChannelNumber();
    }
