    @Override
    public void afterCopyCollection(DBBroker broker, Txn transaction, Collection collection, XmldbURI newUri) throws TriggerException {
        if (comm == null) return;
        comm.callRemoteMethods(new MethodCall(Communicator.AFTER_COPY_COLLECTION, comm.getChannel().getName(), collection.getURI()));
    }
