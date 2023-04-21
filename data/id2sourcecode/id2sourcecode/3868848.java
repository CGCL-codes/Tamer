    private Protocol checkProtocol(Protocol protocol) throws IOException, ProtocolException {
        ClientVersion clientVersion = protocol.read(namedChannel.getChannel(), Protocol.ClientVersion.class);
        clientId = clientVersion.getNodeId();
        FeederManager.Lease lease = feederManager.leases.get(clientId);
        if (lease != null) {
            dbBackup = lease.terminate();
        }
        feederManager.feeders.put(clientId, this);
        if (clientVersion.getVersion() != protocol.getVersion()) {
            String message = "Client requested protocol version: " + clientVersion.getVersion() + " but the server version is " + protocol.getVersion();
            LoggerUtils.warning(logger, feederManager.getEnvImpl(), message);
        }
        protocol.write(protocol.new ServerVersion(), namedChannel);
        return protocol;
    }
