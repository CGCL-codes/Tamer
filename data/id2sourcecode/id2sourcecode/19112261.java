    public void start() throws XMPPException {
        final ConnectionConfiguration configuration = new ConnectionConfiguration(getInfo().getHost(), getInfo().getPort());
        this.connection = new XMPPConnection(configuration);
        this.connection.connect();
        if (getInfo().getResource() != null) {
            this.connection.login(getInfo().getNode(), getInfo().getPassword(), getInfo().getResource());
        } else {
            this.connection.login(getInfo().getNode(), getInfo().getPassword());
        }
        this.connection.addPacketListener(this, null);
    }
