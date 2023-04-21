    private static NodeKey getNodeKeyFromTarget(RemoteObject remoteObject) throws Exception {
        UnicastRef2 ref = (UnicastRef2) remoteObject.getRef();
        LiveRef liveRef = (LiveRef) getField(ref, UnicastRef.class, "ref");
        TCPEndpoint endpoint = (TCPEndpoint) liveRef.getChannel().getEndpoint();
        String host = endpoint.getHost();
        int port = endpoint.getPort();
        return NodeKey.getKey(host, port);
    }
