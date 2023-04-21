    private void processChangeCipherSpec() throws IOException {
        bwmorg.LOG.debug("TlsProtocolHandler: in processChangeCipherSpec() ");
        while (changeCipherSpecQueue.size() > 0) {
            byte[] b = new byte[1];
            changeCipherSpecQueue.read(b, 0, 1, 0);
            changeCipherSpecQueue.removeData(1);
            if (b[0] != 1) {
                bwmorg.LOG.info("TlsProtocolHandler: processChangeCipherSpec() - Error: unexpected message.");
                this.failWithError(AL_fatal, AP_unexpected_message);
            } else {
                if (this.connection_state == CS_CLIENT_FINISHED_SEND) {
                    rs.readSuite = rs.writeSuite;
                    this.connection_state = CS_SERVER_CHANGE_CIPHER_SPEC_RECEIVED;
                } else {
                    bwmorg.LOG.info("TlsProtocolHandler: processChangeCipherSpec() - Error: Not in the correct connection state.");
                    this.failWithError(AL_fatal, AP_handshake_failure);
                }
            }
        }
        bwmorg.LOG.debug("TlsProtocolHandler: done processChangeCipherSpec() ");
    }
