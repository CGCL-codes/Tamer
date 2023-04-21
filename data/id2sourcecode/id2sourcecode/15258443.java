    private void processChangeCipherSpec() throws IOException {
        while (changeCipherSpecQueue.size() > 0) {
            byte[] b = new byte[1];
            changeCipherSpecQueue.read(b, 0, 1, 0);
            changeCipherSpecQueue.removeData(1);
            if (b[0] != 1) {
                this.failWithError(AL_fatal, AP_unexpected_message);
            } else {
                if (this.connection_state == CS_CLIENT_FINISHED_SEND) {
                    rs.readSuite = rs.writeSuite;
                    this.connection_state = CS_SERVER_CHANGE_CIPHER_SPEC_RECEIVED;
                } else {
                    this.failWithError(AL_fatal, AP_handshake_failure);
                }
            }
        }
    }
