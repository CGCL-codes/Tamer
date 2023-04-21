        public void run() throws Exception {
            channel = AppContext.getChannelManager().createChannel(name, listener, Delivery.RELIABLE);
            AppContext.getDataManager().setBinding(name, channel);
        }
