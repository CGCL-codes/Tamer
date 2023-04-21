    private void initContext(Configuration configuration) throws Exception {
        Context context = Context.getInstance();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        context.setClassLoader(classLoader);
        InstProps instProps = configuration.getInstProps();
        context.setInstProps(instProps);
        initMsglets(context, configuration.getMsgletPropsList());
        initDescriptors(context, configuration.getDescriptorPropsList());
        initPackers(context, configuration.getPackerPropsList());
        MsgQueueProps msgQueueProps = configuration.getMsgQueueProps();
        BufferPoolProps bufferPoolProps = configuration.getBufferPoolProps();
        ThreadPoolProps threadPoolProps = configuration.getThreadPoolProps();
        ChannelManagerProps channelManagerProps = configuration.getChannelManagerProps();
        Worker.setBufSize(channelManagerProps.getChannelByteBufferSize());
        context.setBufferFactory(BufferFactoryImpl.getInstance(bufferPoolProps));
        context.setMessageTable(configuration.getMessageTable().initMessageTable());
        context.setRouter(new Router(instProps.getId(), configuration.getRouteTable()));
        context.setWorkshop(new WorkshopImpl(threadPoolProps));
        initMsgQueue(context, msgQueueProps);
        int cltMaxTimeout = initClientManager(context, configuration.getClientPropsList());
        initProcessorManager(context, configuration.getProcessorManagerProps(), configuration.getProcessorPropsList());
        int srvMaxTimeout = initServerManager(context, configuration.getServerManagerProps(), configuration.getServerPropsList());
        ChannelManager channelManager = new ChannelManagerImpl(channelManagerProps, srvMaxTimeout, cltMaxTimeout);
        context.setChannelManager(channelManager);
    }
