    private Protocol negotiateProtocol() throws IOException, ExitException {
        Protocol defaultProtocol = Protocol.getProtocol(repNode);
        ReplicaProtocolVersion message = (ReplicaProtocolVersion) defaultProtocol.read(namedChannel);
        replicaNameIdPair = message.getNameIdPair();
        Feeder dup = repNode.feederManager().getFeeder(replicaNameIdPair.getName());
        if ((dup != null) || (message.getNameIdPair().getName().equals(feederNameIdPair.getName()))) {
            defaultProtocol.write(defaultProtocol.new DuplicateNodeReject("This node: " + replicaNameIdPair + " is already in active use at the feeder "), namedChannel);
            SocketAddress dupAddress = namedChannel.getChannel().socket().getRemoteSocketAddress();
            throw new ExitException("A replica with the id: " + replicaNameIdPair + " is already active with this feeder. " + " The duplicate replica resides at: " + dupAddress);
        }
        final int replicaVersion = message.getVersion();
        Protocol protocol = Protocol.get(repNode, replicaVersion);
        protocol = (protocol == null) ? defaultProtocol : protocol;
        defaultProtocol.write(defaultProtocol.new FeederProtocolVersion(protocol.getVersion()), namedChannel);
        return protocol;
    }
