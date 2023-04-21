    public QueryResponse performGenericQuery(Query query) {
        query = nonNullDelegate().willPerformGenericQuery(this, query);
        if (query == null) {
            return new GenericResponse();
        }
        if (this.getChannel() == null) {
            throw new CayenneRuntimeException("Can't run query - parent DataChannel is not set.");
        }
        return onQuery(this, query);
    }
