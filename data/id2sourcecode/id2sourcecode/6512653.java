    public FederationRequest(SSLSocket federationConnection, BufferedReader reader, BufferedWriter writer, FederationRequestType type) {
        this.federationConnection = federationConnection;
        this.reader = reader;
        this.writer = writer;
        this.type = type;
    }
