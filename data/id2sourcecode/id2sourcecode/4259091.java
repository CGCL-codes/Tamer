    private ChannelServer getChannelServer() {
        final ServiceReference reference = this.context.getServiceReference(ChannelServer.class.getName());
        if (reference == null) {
            throw new RuntimeException("No ChannelServer is registered with the OSGi framework");
        }
        return (ChannelServer) this.context.getService(reference);
    }
