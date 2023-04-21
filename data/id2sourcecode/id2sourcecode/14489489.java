    private synchronized MQQueueManager getQueueManager() throws Exception {
        if (queueManager == null) {
            MQEnvironment.channel = mqCF.getChannel();
            MQEnvironment.port = mqCF.getPort();
            MQEnvironment.hostname = mqCF.getHostName();
            MQEnvironment.properties.put(MQC.TRANSPORT_PROPERTY, MQC.TRANSPORT_MQSERIES);
            if (mqCF.getSecurityExit() != null) {
                Class clazz = getClass().getClassLoader().loadClass(mqCF.getSecurityExit());
                MQSecurityExit securityExit = null;
                if (mqCF.getSecurityExitInit() != null) {
                    securityExit = (MQSecurityExit) clazz.getConstructor(String.class).newInstance(mqCF.getSecurityExitInit());
                } else {
                    securityExit = (MQSecurityExit) clazz.newInstance();
                }
                MQEnvironment.securityExit = securityExit;
            }
            queueManager = new MQQueueManager(mqCF.getQueueManager());
        }
        return queueManager;
    }
