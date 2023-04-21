    public static <T> T synchronizeConditionalWriteThenRead(Object mutex, Condition write_condition, Callback<?> write_callback, Callback<T> read_callback, Callback<?> release_callback) {
        return sync_utils_instance.synchronizeConditionalWriteThenReadImpl(getObjectToLock(mutex), write_condition, write_callback, read_callback, release_callback);
    }
