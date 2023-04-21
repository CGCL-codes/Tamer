    public DataRow getCachedSnapshot(ObjectId oid) {
        if (context != null && context.getChannel() != null) {
            ObjectIdQuery query = new ObjectIdQuery(oid, true, ObjectIdQuery.CACHE_NOREFRESH);
            List results = context.getChannel().onQuery(context, query).firstList();
            return results.isEmpty() ? null : (DataRow) results.get(0);
        } else {
            return null;
        }
    }
