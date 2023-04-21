    @Override
    public void afterCreateCollection(DBBroker broker, Txn transaction, Collection collection) throws TriggerException {
        if (comm == null) return;
        comm.callRemoteMethods(new MethodCall(Communicator.AFTER_CREATE_COLLECTION, comm.getChannel().getName(), collection.getURI()));
    }
