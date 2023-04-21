    protected void AcceptHandler() {
        cb.addPacketListener(new PacketDebug());
        SyncPacketListener sync = new SyncPacketListener(cb);
        Packet p;
        SHA1Helper sha;
        try {
            sha = new SHA1Helper();
        } catch (InstantiationException e) {
            System.err.println("error in creating SHA helper class");
            return;
        }
        cb.disableStreamHeader();
        try {
            cb.connect(InetAddress.getByName(servername), port);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("error in communication for connect request");
            return;
        }
        xsbuilder.setXMLNS(BASE_ACCEPT_XMLNS);
        xsbuilder.setIdentifier(sessionID);
        xsbuilder.setFromAddress(new JID(host));
        xsbuilder.setToAddress(new JID(servername));
        Packet xmlpacket = xsbuilder.build();
        synchronized (sync) {
            System.err.println("sending header");
            cb.send(xmlpacket);
            try {
                System.err.println("waiting for response");
                p = sync.waitForType(xmlpacket, 5000);
                System.err.println("got response");
            } catch (InterruptedException e) {
                p = null;
            }
        }
        if (p == null) throw new RuntimeException("unable to verify server on port");
        System.err.println("computing handshake");
        String SessionID = cb.getSessionID();
        Handshake handshake = new Handshake(sha.digest(SessionID, secret));
        p = null;
        System.err.println("created handshake");
        sync.reset();
        try {
            synchronized (sync) {
                cb.send(handshake);
                System.err.println("waiting for handshake response");
                p = sync.waitForType(handshake, 5000);
                System.err.println("got handshake?!");
            }
        } catch (InterruptedException e) {
            p = null;
        }
        if (p == null) throw new RuntimeException("unable to handshake with server");
        ServerConnect();
    }
