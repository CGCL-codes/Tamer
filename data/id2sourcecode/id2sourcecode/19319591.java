    @Override
    public void beforeMoveCollection(DBBroker broker, Txn transaction, Collection collection, XmldbURI newUri) throws TriggerException {
        if (comm == null) return;
        comm.callRemoteMethods(new MethodCall(Communicator.BEFORE_MOVE_COLLECTION, comm.getChannel().getName(), collection.getURI()));
    }
