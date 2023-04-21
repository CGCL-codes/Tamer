    public synchronized DataRow getSnapshot(ObjectId oid, QueryEngine engine) {
        if (engine instanceof DataChannel) {
            return getSnapshot(oid, (DataChannel) engine);
        } else if (engine instanceof DataContext) {
            return getSnapshot(oid, ((DataContext) engine).getChannel());
        }
        throw new CayenneRuntimeException("QueryEngine is not an DataChannel or DataContext: " + engine);
    }
