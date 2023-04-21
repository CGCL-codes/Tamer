    @Override
    public void beforeCreateDocument(DBBroker broker, Txn transaction, XmldbURI uri) throws TriggerException {
        if (comm == null) return;
        comm.callRemoteMethods(new MethodCall(Communicator.BEFORE_CREATE_DOCUMENT, comm.getChannel().getName(), uri));
    }
