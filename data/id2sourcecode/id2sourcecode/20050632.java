    @Override
    <T> T synchronizeConditionalWriteThenReadImpl(Object mutex, Condition write_condition, Callback<?> write_callback, Callback<T> read_callback, Callback<?> release_callback) {
        if (release_callback != null) throw new QueujException("release_callback cannot be used with legacy sync");
        synchronized (mutex) {
            if (write_condition.isTrue(0)) write_callback.action();
            return read_callback.action();
        }
    }
