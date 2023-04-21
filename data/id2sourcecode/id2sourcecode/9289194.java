    public static <T> T synchronizeWriteThenRead(Object mutex, Callback<?> write_callback, Callback<T> read_callback) {
        return sync_utils_instance.synchronizeWriteThenReadImpl(getObjectToLock(mutex), write_callback, read_callback, null);
    }
