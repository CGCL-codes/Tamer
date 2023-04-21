    public void registerForEvents(Endpoint endpoint) {
        if (endpoint.getNIOChannel() == null) {
            try {
                throw new Exception("Fuck me: " + endpoint);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        if (enableDirectWrites && endpoint.getState() == EPStatus.LIMBO) {
            System.out.println("Adding direct writer handler to endpoint " + endpoint);
            EIOEventHandler writeHandler = controller.getInitializedHandler(EIOEvent.WRITE);
            writeHandler.setIsDedicatedThread(true);
            endpoint.enableDirectWrites(writeHandler);
        }
        if (endpoint.getState() == EPStatus.LIMBO) {
            endpoint.gotoWaitState();
        }
        EIOEventManager em = endpoint.getEventManager();
        if (em == null) {
            em = getEventManager();
            endpoint.setEventManager(em);
        }
        if (endpoint.isOpen() && endpoint.getNIOInterestEvents() != 0 && endpoint.isSelectorized()) {
            em.registerForEvents(endpoint);
        }
    }
