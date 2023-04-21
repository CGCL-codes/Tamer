    public ClientContext getClientContext() {
        if (ww != null) {
            return ww.getClientContext();
        }
        SocketChannelWrapper wrapperObject = new SocketChannelWrapper();
        wrapperObject.setStandardSocket(new Socket().getChannel());
        return new ClientContext(wrapperObject);
    }
