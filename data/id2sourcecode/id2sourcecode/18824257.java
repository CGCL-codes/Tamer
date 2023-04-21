    public MetaServiceChannel addRedirection(String resourcePrefix, String location, Value protocol, Value metadata) throws IOException, FaultException {
        final MetaServiceChannel channel = getChannel();
        Value request = Value.create();
        request.getFirstChild("resourcePrefix").setValue(resourcePrefix);
        request.getFirstChild("location").setValue(location);
        request.getFirstChild("protocol").deepCopy(protocol);
        request.getFirstChild("metadata").deepCopy(metadata);
        channel.send(ADD_REDIRECTION, request);
        Value ret = channel.recv();
        return new MetaServiceChannel(this, '/' + ret.strValue());
    }
