    @Override
    public void afterUpdateDocument(DBBroker broker, Txn transaction, DocumentImpl document) throws TriggerException {
        if (comm == null) return;
        comm.callRemoteMethods(new MethodCall(Communicator.AFTER_UPDATE_DOCUMENT, comm.getChannel().getName(), document.getURI()));
    }
