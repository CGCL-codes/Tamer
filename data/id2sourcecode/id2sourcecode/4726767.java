    protected void disconnectAbstractUser(AbstractUser user) throws RemoteException {
        TCPUser tcpUser = (TCPUser) user;
        try {
            Socket s = tcpUser.getSocket();
            socketUser.remove(s);
            s.getChannel().keyFor(selector).cancel();
            selector.wakeup();
        } catch (Exception e) {
            throw new RemoteException(null, e);
        }
    }
