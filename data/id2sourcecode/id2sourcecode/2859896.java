    @Override
    public void afterDeleteDocument(DBBroker broker, Txn transaction, XmldbURI uri) throws TriggerException {
        if (comm == null) return;
        comm.callRemoteMethods(new MethodCall(Communicator.AFTER_DELETE_DOCUMENT, comm.getChannel().getName(), uri));
    }
