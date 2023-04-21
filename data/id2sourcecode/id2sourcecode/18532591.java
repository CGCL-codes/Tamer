    public TransportHelperFilterStreamCipher(TransportHelper _transport, TransportCipher _read_cipher, TransportCipher _write_cipher) {
        super(_transport);
        read_cipher = _read_cipher;
        write_cipher = _write_cipher;
    }
