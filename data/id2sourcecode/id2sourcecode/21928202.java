    public void startupInterface() {
        setSessionID(JUnique.getUniqueID());
        Common.sessionID = getSessionID();
        Common.sd.setData(getSessionID(), "silentExceptions", "Yes", true);
        Common.applicationMode = "SwingClient";
        JUtility.initLogging("");
        if (JUtility.isValidJavaVersion(Common.requiredJavaVersion) == true) {
            Common.hostList.loadHosts();
            Common.selectedHostID = getHostID();
            while (shutdown == false) {
                logger.debug("Connecting to database.");
                while ((Common.hostList.getHost(getHostID()).isConnected(getSessionID()) == false) && (shutdown == false)) {
                    Common.hostList.getHost(getHostID()).connect(getSessionID(), getHostID());
                }
                if ((Common.hostList.getHost(getHostID()).isConnected(getSessionID()) == true) && (shutdown == false)) {
                    JDBSchema schema = new JDBSchema(getSessionID(), Common.hostList.getHost(getHostID()));
                    schema.validate(false);
                    JDBUser user = new JDBUser(getHostID(), getSessionID());
                    user.setUserId("INTERFACE");
                    user.setPassword("INTERFACE");
                    Common.userList.addUser(getSessionID(), user);
                    if (user.login()) {
                        logger.debug("Starting Threads....");
                        startupThreads();
                        while (shutdown == false) {
                            com.emailer4j.util.JWait.milliSec(1000);
                        }
                        logger.debug("Stopping Threads....");
                        shutdownThreads();
                        user.logout();
                    }
                    logger.debug("Disconnecting from database.");
                    Common.hostList.getHost(getHostID()).disconnectAll();
                }
            }
        }
    }
