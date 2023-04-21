    public void initialize(String id, ConfigMap properties) {
        System.out.println("[BaseBootstrapService] Starting initialization.");
        Session session = (Session) getEntityManager().getDelegate();
        SessionFactory sf = session.getSessionFactory();
        classMetadata = sf.getAllClassMetadata();
        if (classMetadata == null || classMetadata.isEmpty()) return;
        MessageBroker mb = getMessageBroker();
        System.out.println("[BaseBootstrapService] Configuring MessageBroker " + mb.getId());
        List channels = mb.getChannelIds();
        if (channels.isEmpty()) return;
        setupDefaultChannel(mb);
        remotingService = (RemotingService) mb.getService("remoting-service");
        remotingService.addDefaultChannel("my-rtmp");
        messageService = (MessageService) mb.getService("message-service");
        messageService.addDefaultChannel("my-rtmp");
        Set classes = classMetadata.keySet();
        Iterator classIterator = classes.iterator();
        while (classIterator.hasNext()) {
            String className = (String) classIterator.next();
            Class destClass;
            try {
                destClass = Class.forName(className);
                String destName = destClass.getName();
                createClassDestination(destName, destClass);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        createOtherRODestinations();
        System.out.println("[BaseBootstrapService] Finished initialization.");
    }
