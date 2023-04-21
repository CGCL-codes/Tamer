    protected Connection(QueueManager qm) throws MQException {
        this.qm = qm;
        if (!qm.getHostName().toLowerCase().equals("localhost")) {
            MQEnvironment.hostname = qm.getHostName();
        }
        MQEnvironment.channel = qm.getChannel();
        MQEnvironment.port = qm.getPort();
        _queueManager = new MQQueueManager(qm.getQManager());
    }
