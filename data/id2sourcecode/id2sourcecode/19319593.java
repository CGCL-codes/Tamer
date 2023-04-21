    @Override
    public void beforeDeleteCollection(DBBroker broker, Txn transaction, Collection collection) throws TriggerException {
        if (comm == null) return;
        comm.callRemoteMethods(new MethodCall(Communicator.BEFORE_DELETE_COLLECTION, comm.getChannel().getName(), collection.getURI()));
    }
