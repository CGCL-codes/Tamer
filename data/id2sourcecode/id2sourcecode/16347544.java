    @SuppressWarnings("unchecked")
    SQLRow fetchValues(final boolean readCache, final boolean writeCache) {
        final IResultSetHandler handler = new IResultSetHandler(SQLDataSource.MAP_HANDLER) {

            @Override
            public boolean readCache() {
                return readCache;
            }

            @Override
            public boolean writeCache() {
                return writeCache;
            }

            public Set<SQLRow> getCacheModifiers() {
                return Collections.singleton(SQLRow.this);
            }
        };
        this.setValues((Map<String, Object>) this.getTable().getBase().getDataSource().execute(this.getQuery(), handler, false));
        return this;
    }
