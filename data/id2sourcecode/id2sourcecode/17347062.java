    public void run() {
        killableChannels = new LinkedList<GSession>();
        Map<String, MovableObject> channelList = GameState.getChannelList();
        FileLogger.info("SessionCleaner starting......, channelList size=" + channelList.size());
        Iterator it = channelList.keySet().iterator();
        currTime = System.currentTimeMillis();
        while (it.hasNext()) {
            try {
                String sessionID = (String) it.next();
                MovableObject mo = (MovableObject) channelList.get(sessionID);
                SocketChannel client = mo.client;
                if (client != null && client.isConnected()) {
                    lastTime = mo.lastTime;
                    FileLogger.debug("lastTime=" + lastTime);
                    FileLogger.debug("currTime=" + currTime);
                    FileLogger.debug("maxIdleTime=" + maxIdleTime);
                    if (((currTime - lastTime) > (long) maxIdleTime) && !mo.isAdmin) {
                        addKillable(new GSession(sessionID, client));
                    }
                } else {
                    GameState.exitRoom(mo);
                    AccountState.logout(new GSession(sessionID, client));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        killEmAll();
        FastDB.maintanceDBConnection();
        DBProxy.checkConncetions();
    }
