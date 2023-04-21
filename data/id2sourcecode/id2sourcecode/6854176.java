    public Query rewrite(IndexReader reader) throws IOException {
        RangeFilter rangeFilt = new RangeFilter(fieldName, lowerVal != null ? lowerVal : "", upperVal, lowerVal == "" ? false : includeLower, upperVal == null ? false : includeUpper);
        Query q = new ConstantScoreQuery(rangeFilt);
        q.setBoost(getBoost());
        return q;
    }
