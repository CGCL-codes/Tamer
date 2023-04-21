    @Override
    <T> T synchronizeWriteThenReadImpl(Object mutex, Callback<?> write_callback, Callback<T> read_callback, Callback<?> release_callback) {
        if (release_callback != null) throw new QueujException("release_callback cannot be used with legacy sync");
        synchronized (mutex) {
            write_callback.action();
            return read_callback.action();
        }
    }
