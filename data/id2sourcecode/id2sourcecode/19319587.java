    @Override
    public void beforeCreateCollection(DBBroker broker, Txn transaction, XmldbURI uri) throws TriggerException {
        if (comm == null) return;
        comm.callRemoteMethods(new MethodCall(Communicator.BEFORE_CREATE_COLLECTION, comm.getChannel().getName(), uri));
    }
