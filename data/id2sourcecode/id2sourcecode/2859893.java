    @Override
    public void beforeMoveDocument(DBBroker broker, Txn transaction, DocumentImpl document, XmldbURI newUri) throws TriggerException {
        if (comm == null) return;
        comm.callRemoteMethods(new MethodCall(Communicator.BEFORE_MOVE_DOCUMENT, comm.getChannel().getName(), document.getURI()));
    }
