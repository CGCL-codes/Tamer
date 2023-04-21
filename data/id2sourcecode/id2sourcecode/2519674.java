    public void test006_ChannelFilterTest() throws Exception {
        System.out.println(String.format("===== %s =====", getName()));
        new LogService().start();
        new WatchdogService().start();
        Channel<Integer> rawChannel = ChannelFactory.getInstance().createOneToOneChannel("RandomNumbers");
        Channel<Integer> filteredChannel = ChannelFactory.getInstance().createOneToOneChannel("FilteredNumbers");
        ActorFactory.getInstance().createActor(new NumberPrinter(filteredChannel.getReadPort(false)), "NumberPrinter");
        ActorFactory.getInstance().createActor(new ChannelFilter<Integer, Integer>(rawChannel.getReadPort(false), filteredChannel.getWritePort(false), new NumberFilter(), true), "ChannelFilter");
        ActorFactory.getInstance().createActor(new RandomNumberGenerator(rawChannel.getWritePort(false), 500), "RandomNumberGenerator");
        ChannelMonitorOutput.writeDOT(ChannelFactory.getInstance().getChannelMonitor(), System.err);
        Thread.sleep(1000);
    }
