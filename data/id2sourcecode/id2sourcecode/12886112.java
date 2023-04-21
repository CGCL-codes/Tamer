    public boolean isReadyForWrite(EventWaiter waiter) {
        if (waiter != null) {
            write_waiter = waiter;
        }
        return is_ready_for_write;
    }
