    public void broadcastOnControlChannel(SensorMetaData smd) {
        TurbineSrcConfig controlSrcConfig = new TurbineSrcConfig(Constants.LAKE_CONTROL_SOURCE_NAME);
        TurbineSrcConfig currSrcConfig = (TurbineSrcConfig) parser.getSourceConfig(smd.getId());
        TurbineServer currServer = currSrcConfig.getServer();
        TurbineServer controlServer = new TurbineServer(currServer.getServerAddr(), currServer.getUsername(), currServer.getPassword());
        controlSrcConfig.setServer(controlServer);
        controlSrcConfig.setChannelInfo(controlChannelVec, controlChannelDatatypes);
        Debugger.debug(Debugger.TRACE, "Control Information");
        Debugger.debug(Debugger.TRACE, "====================");
        controlSrcConfig.printSrcConfig();
        Debugger.debug(Debugger.TRACE, "====================");
        SiteSource src = new SiteSource(controlSrcConfig, null);
        Vector<Integer> chnlIndex = src.getChannelIndicies();
        src.connect();
        Debugger.debug(Debugger.TRACE, "====================");
        String serverAddr = currServer.getServerAddr();
        String portStr = "";
        int indexOfPort = serverAddr.indexOf(":");
        if (indexOfPort != -1) {
            portStr = serverAddr.substring(indexOfPort, serverAddr.length());
            serverAddr = serverAddr.substring(0, indexOfPort);
        }
        if ("127.0.0.1".equals(serverAddr) || "localhost".equals(serverAddr)) {
            serverAddr = Utility.getIPAddress(serverAddr);
            System.err.println("WARNING: WARNING: WARNING: Server address is local " + "address. IP address being sent (" + serverAddr + ") to sink may be " + "a loopback address. " + "In such a case, sink WILL not work if it is not on the same machine " + "as the server");
        }
        Debugger.debug(Debugger.TRACE, "PortStr= " + portStr);
        Debugger.debug(Debugger.TRACE, "ServerAddr= " + serverAddr);
        String username = currServer.getUsername();
        String password = currServer.getPassword();
        if ("".equals(username)) {
            username = Constants.NONEMPTY_DUMMY_USER_NAME_OR_PASSWORD;
        }
        if ("".equals(password)) {
            password = Constants.NONEMPTY_DUMMY_USER_NAME_OR_PASSWORD;
        }
        String message = Constants.LAKE_CONTROL_LOOKUP_PREFIX + smd.getId() + Constants.LAKE_CONTROL_SEPARATOR + serverAddr + portStr + Constants.LAKE_CONTROL_SEPARATOR + username + Constants.LAKE_CONTROL_SEPARATOR + password + Constants.LAKE_CONTROL_SEPARATOR + smd.getWebServiceString();
        src.insertData(chnlIndex.elementAt(0).intValue(), (Object) message);
        src.flush();
        src.disconnect();
    }
