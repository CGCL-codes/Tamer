    @Override
    public void afterDeleteCollection(DBBroker broker, Txn transaction, XmldbURI uri) throws TriggerException {
        if (comm == null) return;
        comm.callRemoteMethods(new MethodCall(Communicator.AFTER_DELETE_COLLECTION, comm.getChannel().getName(), uri));
    }
