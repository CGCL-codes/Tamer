    public void resolveHollow(DataObject object) {
        if (object.getPersistenceState() != PersistenceState.HOLLOW) {
            return;
        }
        DataContext context = object.getDataContext();
        if (context == null) {
            object.setPersistenceState(PersistenceState.TRANSIENT);
            return;
        }
        synchronized (this) {
            ObjectIdQuery query = new ObjectIdQuery(object.getObjectId(), false, ObjectIdQuery.CACHE);
            List results = context.getChannel().onQuery(context, query).firstList();
            if (results.size() == 0) {
                processDeletedID(object.getObjectId());
            } else if (object.getPersistenceState() == PersistenceState.HOLLOW) {
                query = new ObjectIdQuery(object.getObjectId(), false, ObjectIdQuery.CACHE_REFRESH);
                results = context.getChannel().onQuery(context, query).firstList();
                if (results.size() == 0) {
                    processDeletedID(object.getObjectId());
                }
            }
        }
    }
