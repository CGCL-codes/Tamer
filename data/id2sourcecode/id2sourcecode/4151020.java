    protected BigInteger getChannelId(final String channelName) throws Exception {
        return KernelCallable.call(new KernelCallable<BigInteger>("getChannelId") {

            public BigInteger call() throws Exception {
                Channel channel = AppContext.getChannelManager().getChannel(channelName);
                Field field = getField(channel.getClass(), "channelRef");
                ManagedReference channelRef = (ManagedReference) field.get(channel);
                return channelRef.getId();
            }
        }, txnScheduler, taskOwner);
    }
