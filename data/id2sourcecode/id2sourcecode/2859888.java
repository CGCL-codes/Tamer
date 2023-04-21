    @Override
    public void afterCreateDocument(DBBroker broker, Txn transaction, DocumentImpl document) throws TriggerException {
        if (comm == null) return;
        comm.callRemoteMethods(new MethodCall(Communicator.AFTER_CREATE_DOCUMENT, comm.getChannel().getName(), document.getURI()));
    }
