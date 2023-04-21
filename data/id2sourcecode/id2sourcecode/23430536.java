        public void requestToCorrelate(ChannelWrapper wrapper) {
            wrapper.addConnectionListener(this);
            if (wrapper.isConnected()) {
                connectionMade(wrapper.getChannel());
            } else {
                wrapper.requestConnection();
            }
        }
