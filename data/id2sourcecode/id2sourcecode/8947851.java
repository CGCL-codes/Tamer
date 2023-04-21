    private static boolean performTestDDA(final FcpSocket.DDAModes mode, final File dir, final FcpSocket fcpSocket, final FcpListenThreadConnection listenThread) {
        if (mode == null || dir == null) {
            return false;
        }
        if (!Core.frostSettings.getBoolValue(SettingsClass.FCP2_USE_DDA)) {
            return false;
        }
        if ((listenThread == null && fcpSocket == null) || (listenThread != null && fcpSocket != null)) {
            logger.severe("TestDDA: Invalid call, listenThread and fcpSocket both null or both set.");
            return false;
        }
        final Set<String> checkedDirectories;
        if (listenThread != null) {
            checkedDirectories = listenThread.getCheckedDirectories();
        } else {
            checkedDirectories = fcpSocket.getCheckedDirectories();
        }
        if (checkedDirectories.contains(dir.getAbsolutePath())) {
            logger.warning("TestDDA: directory is already permitted: " + dir.getAbsolutePath());
            return true;
        }
        final WaitingNodeMessageListener nodeMessageListener;
        if (listenThread != null) {
            listenThread.aquireFcpWriteLock();
            nodeMessageListener = new WaitingNodeMessageListener();
            listenThread.addNodeMessageListener(nodeMessageListener);
        } else {
            nodeMessageListener = null;
        }
        try {
            {
                List<String> sendNodeMsg = new ArrayList<String>();
                sendNodeMsg.add("TestDDARequest");
                sendNodeMsg.add("Directory=" + dir.getAbsolutePath());
                sendNodeMsg.add("WantReadDirectory=true");
                sendNodeMsg.add("WantWriteDirectory=true");
                if (listenThread != null) {
                    nodeMessageListener.reset();
                    listenThread.sendMessage(sendNodeMsg);
                } else {
                    NodeMessage.sendMessage(sendNodeMsg, fcpSocket.getFcpOut());
                }
            }
            NodeMessage nodeMsg = null;
            nodeMsg = receiveNodeMessage(fcpSocket, listenThread, nodeMessageListener);
            if (nodeMsg == null || !nodeMsg.isMessageName("TestDDAReply")) {
                logger.warning("TestDDA failed, TestDDAReply expected: " + nodeMsg);
                return false;
            }
            if (!dir.getAbsolutePath().equals(nodeMsg.getStringValue("Directory"))) {
                logger.warning("TestDDA failed, different directory returned: " + nodeMsg);
                return false;
            }
            final String readFilename = nodeMsg.getStringValue("ReadFilename");
            final String writeFilename = nodeMsg.getStringValue("WriteFilename");
            final String contentToWrite = nodeMsg.getStringValue("ContentToWrite");
            if (readFilename == null || writeFilename == null || contentToWrite == null) {
                logger.warning("TestDDA failed, invalid parameters returned: " + nodeMsg);
                return false;
            }
            final File readFile = new File(readFilename);
            final File writeFile = new File(writeFilename);
            if (!FileAccess.writeFile(contentToWrite, writeFile, "UTF-8")) {
                logger.warning("TestDDA failed, could not write requested writeFile: " + nodeMsg);
                return false;
            }
            final String readFileContent = FileAccess.readFile(readFile, "UTF-8").trim();
            {
                List<String> sendNodeMsg = new ArrayList<String>();
                sendNodeMsg.add("TestDDAResponse");
                sendNodeMsg.add("Directory=" + dir.getAbsolutePath());
                sendNodeMsg.add("ReadContent=" + readFileContent);
                if (listenThread != null) {
                    nodeMessageListener.reset();
                    listenThread.sendMessage(sendNodeMsg);
                } else {
                    NodeMessage.sendMessage(sendNodeMsg, fcpSocket.getFcpOut());
                }
            }
            nodeMsg = receiveNodeMessage(fcpSocket, listenThread, nodeMessageListener);
            writeFile.delete();
            if (nodeMsg == null || !nodeMsg.isMessageName("TestDDAComplete")) {
                logger.warning("TestDDA failed, TestDDAComplete expected: " + nodeMsg);
                return false;
            }
            if (!dir.getAbsolutePath().equals(nodeMsg.getStringValue("Directory"))) {
                logger.warning("TestDDA failed, different directory returned: " + nodeMsg);
                return false;
            }
            boolean readDirectoryAllowed = nodeMsg.getBoolValue("ReadDirectoryAllowed");
            boolean writeDirectoryAllowed = nodeMsg.getBoolValue("WriteDirectoryAllowed");
            if (!readDirectoryAllowed || !writeDirectoryAllowed) {
                logger.warning("TestDDA completed, DDA not permitted: " + nodeMsg);
                return false;
            }
            checkedDirectories.add(dir.getAbsolutePath());
            if (Logging.inst().doLogFcp2Messages()) {
                logger.warning("TestDDA: DDA permitted for dir='" + dir.getAbsolutePath() + "'");
            }
            System.out.println("DDA permitted for dir='" + dir.getAbsolutePath() + "'");
            return true;
        } finally {
            if (listenThread != null) {
                listenThread.removeNodeMessageListener(nodeMessageListener);
                listenThread.releaseFcpWriteLock();
            }
        }
    }
