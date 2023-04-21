    protected void handshakeComplete() throws IOException {
        if (selected_protocol == CRYPTO_PLAIN) {
            filter = new TransportHelperFilterTransparent(transport, true);
        } else if (selected_protocol == CRYPTO_XOR) {
            filter = new TransportHelperFilterStreamXOR(transport, secret_bytes);
        } else if (selected_protocol == CRYPTO_RC4) {
            filter = new TransportHelperFilterStreamCipher(transport, read_cipher, write_cipher);
        } else {
            throw (new IOException("Invalid selected protocol '" + selected_protocol + "'"));
        }
        if (initial_data_in != null) {
            filter = new TransportHelperFilterInserter(filter, initial_data_in);
        }
        handshake_complete = true;
    }
