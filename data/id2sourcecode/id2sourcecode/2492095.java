            public void run() {
                ManagedChannelListener listener = new ManagedChannelListener();
                Channel channel = AppContext.getChannelManager().createChannel(channelName, listener, Delivery.RELIABLE);
                DataManager dataManager = AppContext.getDataManager();
                dataManager.setBinding(channelName, channel);
                dataManager.setBinding(listenerName, listener);
            }
