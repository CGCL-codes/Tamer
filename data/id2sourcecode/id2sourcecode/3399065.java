    protected RecordStream(TlsProtocolHandler handler, InputStream is, OutputStream os) {
        this.handler = handler;
        this.is = is;
        this.os = os;
        hash1 = new CombinedHash();
        hash2 = new CombinedHash();
        this.readSuite = new TlsNullCipherSuite();
        this.writeSuite = this.readSuite;
    }
