    public void startupSnmp(InetAddress address, int port, int trapPort, String readCommunity, String writeCommunity) throws SocketException {
        if (DEBUG) {
            System.out.println("startupSnmp(" + address + "," + port + "," + readCommunity + "," + writeCommunity + ")");
        }
        SnmpPeer peer = new SnmpPeer(address, port);
        SnmpParameters params = peer.getParameters();
        params.setVersion(SnmpSMI.SNMPV2);
        params.setReadCommunity(readCommunity);
        params.setWriteCommunity(writeCommunity);
        peer.setParameters(params);
        agentSession = new SnmpAgentSession(this, peer);
        trapSession = new SnmpTrapSession(this, trapPort);
    }
