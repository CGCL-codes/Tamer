    public MetaServiceChannel loadEmbeddedJolieService(String resourcePrefix, String filepath, Value metadata) throws IOException, FaultException {
        final MetaServiceChannel channel = getChannel();
        Value request = Value.create();
        request.getFirstChild("resourcePrefix").setValue(resourcePrefix);
        request.getFirstChild("filepath").setValue(filepath);
        request.getFirstChild("metadata").deepCopy(metadata);
        channel.send(LOAD_EMBEDDED_JOLIE_SERVICE, request);
        Value ret = channel.recv();
        return new MetaServiceChannel(this, '/' + ret.strValue());
    }
