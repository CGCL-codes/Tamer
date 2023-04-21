    protected boolean readyForWrite(boolean ready) {
        if (trace) {
            TimeFormatter.milliTrace("trans: readyForWrite -> " + ready);
        }
        if (ready) {
            boolean progress = !is_ready_for_write;
            is_ready_for_write = true;
            EventWaiter ww = write_waiter;
            if (ww != null) {
                ww.eventOccurred();
            }
            return progress;
        } else {
            is_ready_for_write = false;
            return (false);
        }
    }
