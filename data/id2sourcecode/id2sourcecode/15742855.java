    @Override
    public Query rewrite(Query query) throws SearchLibException {
        if (!online) throw new SearchLibException("Index is offline");
        rwl.r.lock();
        try {
            if (reader != null) return reader.rewrite(query);
            return null;
        } finally {
            rwl.r.unlock();
        }
    }
