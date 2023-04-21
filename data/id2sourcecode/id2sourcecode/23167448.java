    private Protocol negotiateProtocol() throws IOException {
        final Protocol defaultProtocol = Protocol.getProtocol(repNode);
        defaultProtocol.write(defaultProtocol.new ReplicaProtocolVersion(), namedChannel);
        Message message = defaultProtocol.read(namedChannel);
        if (message instanceof DuplicateNodeReject) {
            throw new EnvironmentFailureException(repNode.getRepImpl(), EnvironmentFailureReason.HANDSHAKE_ERROR, "A replica with the name: " + replicaNameIdPair + " is already active with the Feeder:" + feederNameIdPair);
        }
        FeederProtocolVersion feederVersion = ((FeederProtocolVersion) message);
        feederNameIdPair = feederVersion.getNameIdPair();
        Protocol configuredProtocol = Protocol.get(repNode, feederVersion.getVersion());
        LoggerUtils.fine(logger, repNode.getRepImpl(), "Feeder id: " + feederVersion.getNameIdPair() + "Response message: " + feederVersion.getVersion());
        namedChannel.setNameIdPair(feederNameIdPair);
        LoggerUtils.fine(logger, repNode.getRepImpl(), "Channel Mapping: " + feederNameIdPair + " is at " + namedChannel.getChannel());
        if (configuredProtocol == null) {
            throw new EnvironmentFailureException(repNode.getRepImpl(), EnvironmentFailureReason.PROTOCOL_VERSION_MISMATCH, "Incompatible protocol versions. " + "Version: " + feederVersion.getVersion() + " requested by the Feeder: " + feederNameIdPair + " is not supported by this Replica: " + replicaNameIdPair + ", which is at version: " + defaultProtocol.getVersion());
        }
        return configuredProtocol;
    }
