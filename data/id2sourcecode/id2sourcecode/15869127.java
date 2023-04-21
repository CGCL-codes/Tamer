    public MulticastEventService(QMass qmass, DiscoveryService discoveryService, InetSocketAddress listening) {
        QMassIR ir = qmass.getIR();
        try {
            clusterAddress = InetAddress.getByName(ir.getMulticastAddress());
            readPort = ir.getMulticastReadPort();
            int writePort = ir.getMulticastWritePort();
            outSocket = createDatagramSocket(writePort);
            inSocket = new MulticastSocket(readPort);
            inSocket.joinGroup(clusterAddress);
            inSocket.setSoTimeout(1);
            this.listening = listening;
            this.greetService = new DefaultGreetService(qmass, this);
            this.leaveService = new DefaultLeaveService(qmass, this);
            this.discoveryService = discoveryService;
            qmass.registerService(this);
            logger.info(getListening() + " multicast " + clusterAddress + " read " + readPort + ", write " + writePort);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
