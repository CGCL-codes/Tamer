    GraphDiff flushToParent(boolean cascade) {
        if (this.getChannel() == null) {
            throw new CayenneRuntimeException("Cannot commit changes - channel is not set.");
        }
        int syncType = cascade ? DataChannel.FLUSH_CASCADE_SYNC : DataChannel.FLUSH_NOCASCADE_SYNC;
        synchronized (getObjectStore()) {
            DataContextFlushEventHandler eventHandler = null;
            ObjectStoreGraphDiff changes = getObjectStore().getChanges();
            boolean noop = isValidatingObjectsOnCommit() ? changes.validateAndCheckNoop() : changes.isNoop();
            if (noop) {
                getObjectStore().postprocessAfterPhantomCommit();
                return new CompoundDiff();
            }
            if (isTransactionEventsEnabled()) {
                eventHandler = new DataContextFlushEventHandler(this);
                eventHandler.registerForDataContextEvents();
                fireWillCommit();
            }
            try {
                GraphDiff returnChanges = getChannel().onSync(this, changes, syncType);
                getObjectStore().postprocessAfterCommit(returnChanges);
                fireTransactionCommitted();
                fireDataChannelCommitted(this, changes);
                if (!returnChanges.isNoop()) {
                    fireDataChannelCommitted(getChannel(), returnChanges);
                }
                return returnChanges;
            } catch (CayenneRuntimeException ex) {
                fireTransactionRolledback();
                Throwable unwound = Util.unwindException(ex);
                if (unwound instanceof CayenneRuntimeException) {
                    throw (CayenneRuntimeException) unwound;
                } else {
                    throw new CayenneRuntimeException("Commit Exception", unwound);
                }
            } finally {
                if (isTransactionEventsEnabled()) {
                    eventHandler.unregisterFromDataContextEvents();
                }
            }
        }
    }
