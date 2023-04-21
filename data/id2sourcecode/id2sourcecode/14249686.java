    private static void addPublisher(IEventPublisher ep) {
        AbstractReferenceReadWriteSet arrs = readwriteset.arrs;
        AbstractReferenceReadWriteSet arws = readwriteset.arws;
        if (arrs.count > 0 || arws.count > 0) ep.addChanges(readwriteset);
        if (publisherSize == 0) {
            eps = new IEventPublisher[2];
            eps[publisherSize++] = ep;
            return;
        }
        ensurePublishersCapacity(publisherSize + 1);
        eps[publisherSize++] = ep;
    }
