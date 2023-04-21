            public BigInteger call() throws Exception {
                Channel channel = AppContext.getChannelManager().getChannel(channelName);
                Field field = getField(channel.getClass(), "channelRef");
                ManagedReference channelRef = (ManagedReference) field.get(channel);
                return channelRef.getId();
            }
