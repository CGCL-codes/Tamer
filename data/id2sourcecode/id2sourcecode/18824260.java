    public void unloadEmbeddedJolieService(String resourceName) throws IOException {
        final MetaServiceChannel channel = getChannel();
        channel.send(UNLOAD_EMBEDDED_JOLIE_SERVICE, Value.create(resourceName));
        try {
            channel.recv();
        } catch (FaultException f) {
            throw new IOException(f);
        }
    }
