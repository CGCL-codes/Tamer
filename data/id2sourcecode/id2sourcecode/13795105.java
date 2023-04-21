    protected MqConnection(MqQueueManager qm) throws MQException {
        this.qm = qm;
        MQEnvironment.hostname = qm.getHostName();
        MQEnvironment.channel = qm.getChannel();
        MQEnvironment.port = qm.getPort();
        _queueManager = new MQQueueManager(qm.getQManager());
    }
