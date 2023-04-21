    public void removeRedirection(String resourceName) throws IOException {
        final MetaServiceChannel channel = getChannel();
        channel.send(REMOVE_REDIRECTION, Value.create(resourceName));
        try {
            channel.recv();
        } catch (FaultException f) {
            throw new IOException(f);
        }
    }
