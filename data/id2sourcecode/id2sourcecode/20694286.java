    public void start() throws Exception {
        if (configuration == null || configSet == false) {
            if (configResourcePath == null) {
                configuration = new MessageServiceConfiguration();
            } else {
                URL url = getClass().getClassLoader().getResource(configResourcePath);
                if (url == null) {
                    url = new URL(configResourcePath);
                }
                JAXBContext jaxb = JAXBContext.newInstance(MessageServiceConfiguration.class);
                Reader reader = new InputStreamReader(url.openStream());
                configuration = (MessageServiceConfiguration) jaxb.createUnmarshaller().unmarshal(reader);
            }
        }
        if (registry == null) {
            try {
                registry = new JndiComponentRegistry();
            } catch (Exception e) {
                System.err.println("Warning: Failed to instantiate an InitialContext for binding created queues/topics.");
            }
        }
        if (threadPool == null) threadPool = Executors.newCachedThreadPool();
        timeoutTaskInterval = configuration.getTimeoutTaskInterval();
        timeoutTask = new TimeoutTask(timeoutTaskInterval);
        threadPool.execute(timeoutTask);
        DestinationSettings defaultSettings = new DestinationSettings();
        defaultSettings.setConsumerSessionTimeoutSeconds(configuration.getConsumerSessionTimeoutSeconds());
        defaultSettings.setDuplicatesAllowed(configuration.isDupsOk());
        defaultSettings.setDurableSend(configuration.isDefaultDurableSend());
        HashMap<String, Object> transportConfig = new HashMap<String, Object>();
        transportConfig.put(TransportConstants.SERVER_ID_PROP_NAME, configuration.getInVmId());
        ClientSessionFactory consumerSessionFactory = new ClientSessionFactoryImpl(new TransportConfiguration(InVMConnectorFactory.class.getName(), transportConfig));
        if (configuration.getConsumerWindowSize() != -1) {
            consumerSessionFactory.setConsumerWindowSize(configuration.getConsumerWindowSize());
        }
        ClientSessionFactory sessionFactory = new ClientSessionFactoryImpl(new TransportConfiguration(InVMConnectorFactory.class.getName(), transportConfig));
        LinkStrategy linkStrategy = new LinkHeaderLinkStrategy();
        if (configuration.isUseLinkHeaders()) {
            linkStrategy = new LinkHeaderLinkStrategy();
        } else {
            linkStrategy = new CustomHeaderLinkStrategy();
        }
        queueManager.setSessionFactory(sessionFactory);
        queueManager.setTimeoutTask(timeoutTask);
        queueManager.setConsumerSessionFactory(consumerSessionFactory);
        queueManager.setDefaultSettings(defaultSettings);
        queueManager.setPushStoreFile(configuration.getQueuePushStoreDirectory());
        queueManager.setProducerPoolSize(configuration.getProducerSessionPoolSize());
        queueManager.setLinkStrategy(linkStrategy);
        queueManager.setRegistry(registry);
        topicManager.setSessionFactory(sessionFactory);
        topicManager.setTimeoutTask(timeoutTask);
        topicManager.setConsumerSessionFactory(consumerSessionFactory);
        topicManager.setDefaultSettings(defaultSettings);
        topicManager.setPushStoreFile(configuration.getTopicPushStoreDirectory());
        topicManager.setProducerPoolSize(configuration.getProducerSessionPoolSize());
        topicManager.setLinkStrategy(linkStrategy);
        topicManager.setRegistry(registry);
        queueManager.start();
        topicManager.start();
    }
