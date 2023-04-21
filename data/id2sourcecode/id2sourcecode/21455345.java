    public List getChannelsList() throws NotAuthenticatedException, NetworkException, ProtocolException {
        if (authenticated == false) {
            throw new NotAuthenticatedException();
        }
        return null;
    }
