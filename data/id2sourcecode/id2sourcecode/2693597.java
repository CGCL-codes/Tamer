    public void createNIO() throws IOException {
        Selector selector = Selector.open();
        Channel channel = ChannelFactory.instance().getChannel();
        Queue nioQueue = new Queue(new CompositeQueueAdmissionsController(), channel);
        SelectorRunnable nioRunnable = new SelectorRunnable(selector, channel, manager);
        Thread thread = new Thread(nioRunnable);
        thread.start();
        manager.setNioTaskQueue(nioQueue);
    }
