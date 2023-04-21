    @Override
    public void afterCopyDocument(DBBroker broker, Txn transaction, DocumentImpl document, XmldbURI newUri) throws TriggerException {
        if (comm == null) return;
        comm.callRemoteMethods(new MethodCall(Communicator.AFTER_COPY_DOCUMENT, comm.getChannel().getName(), document.getURI()));
    }
