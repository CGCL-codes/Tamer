    private void doStart(boolean retry) throws Exception {
        if (channelServer == null) {
            String coosInstanceName = (String) properties.get(PROPERTY_COOS_INSTANCE_NAME);
            COOS coos;
            if (coosInstanceName != null) {
                coos = COOSFactory.getCOOSInstance(coosInstanceName);
                while (((coos = COOSFactory.getCOOSInstance(coosInstanceName)) == null) && retry) {
                    logger.warn("Establishing transport to JVM coos " + coosInstanceName + " failed. Retrying in " + retryTime + " millisec.");
                    Thread.sleep(retryTime);
                }
                if (coos == null) {
                    throw new NullPointerException("No COOS instance " + coosInstanceName + " defined in this vm!");
                }
            } else {
                coos = COOSFactory.getDefaultCOOSInstance();
                while (((coos = COOSFactory.getDefaultCOOSInstance()) == null) && retry) {
                    logger.warn("Establishing transport to JVM defaultCOOS failed. Retrying in " + retryTime + " millisec.");
                    Thread.sleep(retryTime);
                }
                if (coos == null) {
                    throw new NullPointerException("No defaultCOOS defined in this vm!");
                }
            }
            String channelServerName = (String) properties.get(PROPERTY_CHANNEL_SERVER_NAME);
            if (channelServerName == null) {
                channelServerName = "default";
            }
            channelServer = coos.getChannelServer(channelServerName);
            if (channelServer == null) {
                if (!retry) throw new NullPointerException("ChannelServer: " + channelServerName + " is not declared within COOS instance: " + coosInstanceName);
                while (((channelServer = coos.getChannelServer(channelServerName)) == null) && retry) {
                    Thread.sleep(retryTime);
                    logger.warn("Establishing transport to JVM channelserver failed. Retrying in " + retryTime + " millisec.");
                }
            }
            logger.debug("Established transport");
        }
        running = true;
        intr.start();
        channelServer.initializeChannel(intr);
        if (storedConnectMsg != null) {
            intr.chainedProcessor.processMessage(storedConnectMsg);
            storedConnectMsg = null;
        }
    }
