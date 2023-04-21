    @Override
    public void send(String payload, TransportReceiver receiver) {
        if (this.transport == null) {
            receiver.onTransportFailure(new ServerFailure("Must call create() before making requests."));
        }
        transport.send(payload, receiver);
    }
