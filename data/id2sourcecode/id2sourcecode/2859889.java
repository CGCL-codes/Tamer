    @Override
    public void beforeUpdateDocument(DBBroker broker, Txn transaction, DocumentImpl document) throws TriggerException {
        if (comm == null) return;
        comm.callRemoteMethods(new MethodCall(Communicator.BEFORE_UPDATE_DOCUMENT, comm.getChannel().getName(), document.getURI()));
    }
