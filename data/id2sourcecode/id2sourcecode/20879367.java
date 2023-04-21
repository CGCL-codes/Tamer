    public Promise<Void> waitWritable(SelectionKey key) {
        Promise<Void> rc = new Promise<Void>();
        AResolver<Void> resolver = rc.resolver();
        try {
            Attachment a = (Attachment) key.attachment();
            if (a.writeWaiter != null) throw new IllegalStateException("someone already waits for write");
            a.writeWaiter = resolver;
            a.updateOps();
        } catch (Throwable t) {
            resolver.smash(t);
        }
        return rc;
    }
