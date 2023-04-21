    public final boolean parseCommand(String command) {
        Worker worker = getServerWorker();
        RemoteClient clientConfig = getRemoteClient();
        String[] cmd = extractRegexGroups("^SET\\s*CONNECTION\\s*(.*?)\\s*=\\s*(.*?)$", command.toUpperCase());
        if (cmd[0].toUpperCase().matches("GZIP")) {
            if (cmd[1].toUpperCase().matches("ON")) {
                clientConfig.setCompressionMode(RemoteClient.COMPRESSION_GZIP);
                worker.sendMessage(Protocol.MSG_OK, "GZIP Compression enabled.");
                logManager.writeToLog(4, "NET", "[" + worker.getThreadID() + "] Enabled GZIP compression.");
                return true;
            } else if (cmd[1].toUpperCase().matches("OFF")) {
                clientConfig.setCompressionMode(RemoteClient.COMPRESSION_NONE);
                worker.sendMessage(Protocol.MSG_OK, "GZIP Compression disabled.");
                logManager.writeToLog(4, "NET", "[" + worker.getThreadID() + "] Disabled GZIP compression.");
                return true;
            }
        }
        if (cmd[0].toUpperCase().matches("LZIP")) {
            if (cmd[1].toUpperCase().matches("ON")) {
                clientConfig.setCompressionMode(RemoteClient.COMPRESSION_LZIP);
                worker.sendMessage(Protocol.MSG_OK, "LZIP Compression enabled.");
                logManager.writeToLog(4, "NET", "[" + worker.getThreadID() + "] Enabled LZIP compression.");
                return true;
            } else if (cmd[1].toUpperCase().matches("OFF")) {
                clientConfig.setCompressionMode(RemoteClient.COMPRESSION_NONE);
                worker.sendMessage(Protocol.MSG_OK, "LZIP Compression disabled.");
                logManager.writeToLog(4, "NET", "[" + worker.getThreadID() + "] Disabled LZIP compression.");
                return true;
            }
        }
        return false;
    }
