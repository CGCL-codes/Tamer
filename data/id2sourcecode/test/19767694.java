    public boolean mergeAfter(final Map<Value<?>, Object> reads, final Map<Value.Mutable<?>, Object> writes) {
        final boolean success = Collections.disjoint(getWrites().keySet(), reads.keySet());
        if (success) {
            getWrites().putAll(writes);
            reads.putAll(reads);
        }
        return success;
    }
