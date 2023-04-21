    public void connect() {
        try {
            ConnectionConfiguration conf = new ConnectionConfiguration("mobilis.inf.tu-dresden.de");
            mConnection = new XMPPConnection(conf);
            mConnection.connect();
            mConnection.login(mXMPPUser, mPassword);
            mConnection.addPacketListener(new MessageListener(), new PacketTypeFilter(Message.class));
            mConnection.addPacketListener(new IQListener(), new PacketTypeFilter(IQ.class));
            ProviderManager.getInstance().addIQProvider("ping", "urn:xmpp:ping", new PingIQProvder());
            ProviderManager.getInstance().addIQProvider("test", "mobilis:mxa:test", new TestIQProvder());
            FileTransferManager ftm = new FileTransferManager(mConnection);
            ftm.addFileTransferListener(new FileListener());
        } catch (XMPPException e) {
            e.printStackTrace();
            return;
        }
    }
