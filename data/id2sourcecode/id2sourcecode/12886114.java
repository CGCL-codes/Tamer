    protected void writeFailed(Throwable msg) {
        msg.fillInStackTrace();
        write_select_failure = msg;
        is_ready_for_write = true;
    }
