    private void checkClockSkew(Protocol protocol) throws IOException, ProtocolException {
        SNTPRequest request = null;
        do {
            request = protocol.read(namedChannel.getChannel(), SNTPRequest.class);
            protocol.write(protocol.new SNTPResponse(request), namedChannel);
        } while (!request.isLast());
    }
