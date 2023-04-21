    @Override
    public void run() {
        Base.writeLogg(THIS_MIDDLEWARE, new LB("starting thread"));
        Enumeration<String> enm;
        BridgeHandler bridge;
        String bridgeID;
        boolean event;
        while (true) {
            enm = clients.keys();
            event = false;
            while (enm.hasMoreElements()) {
                bridgeID = enm.nextElement();
                if (Conf.getItem(THIS_MIDDLEWARE, bridgeID).isEnabled()) {
                    Base.writeLogg(THIS_MIDDLEWARE, new LB("event:" + bridgeID));
                    bridge = clients.get(bridgeID);
                    try {
                        if (bridge.getSubmitQueue().size() > 0) {
                            submit(bridge);
                            event = true;
                        }
                        if (bridge.isEnabledNextEvent(LAST_ACTIVATE_TIMESTAMP)) {
                            getStatus(bridge);
                            download(bridge);
                            delete(bridge);
                            bridge.setEventTimeStamp();
                            event = true;
                        }
                    } catch (Exception e) {
                        Base.writeLogg(THIS_MIDDLEWARE, new LB(e));
                        try {
                            setConfiguration();
                        } catch (Exception e0) {
                            Base.writeLogg(THIS_MIDDLEWARE, new LB(e0));
                        }
                    }
                }
            }
            if (!event) {
                Base.writeLogg(THIS_MIDDLEWARE, new LB("sleep:" + LAST_ACTIVATE_TIMESTAMP));
                try {
                    sleep(LAST_ACTIVATE_TIMESTAMP);
                } catch (InterruptedException e) {
                    Base.writeLogg(THIS_MIDDLEWARE, new LB(e));
                }
            }
        }
    }
