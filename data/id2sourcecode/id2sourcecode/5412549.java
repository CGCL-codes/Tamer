    public void testScrap() throws Exception {
        System.out.println(channel.getChannelReceiver().getClass());
        ((ReceiverBase) channel.getChannelReceiver()).setMaxThreads(1);
    }
