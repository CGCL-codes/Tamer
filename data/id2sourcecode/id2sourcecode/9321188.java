        public void run() {
            if (internalMessage == null || internalMessage.getDataSize() < 1) {
                close();
                return;
            }
            Channel channel = Context.getInstance().getChannelManager().getServerChannel(internalMessage.getOriginalChannelId());
            if (channel != null) {
                channel.reactivate();
                Buffer data = internalMessage.detachData();
                try {
                    channel.write(data);
                } catch (ChannelClosedException e) {
                }
            }
            close();
        }
