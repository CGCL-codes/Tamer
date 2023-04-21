    @Override
    public void beforeCopyDocument(DBBroker broker, Txn transaction, DocumentImpl document, XmldbURI newUri) throws TriggerException {
        if (comm == null) return;
        comm.callRemoteMethods(new MethodCall(Communicator.BEFORE_COPY_DOCUMENT, comm.getChannel().getName(), document.getURI()));
    }
