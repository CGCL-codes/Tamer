    public final void writeLog(Level lvl, long thread_id, String description, String details) {
        if (lvl == DEV && log_dev) {
            write(DEV.toString(), thread_id, description, details);
        }
        if (lvl == TRACE && log_trace) {
            write(TRACE.toString(), thread_id, description, details);
        }
        if (lvl == DEBUG && log_debug) {
            write(DEBUG.toString(), thread_id, description, details);
        }
        if (lvl == CRITICAL && log_critical) {
            write(CRITICAL.toString(), thread_id, description, details);
            ThreadSchedule.haltBlocking();
        }
        if (lvl == COMM && log_comm) {
            write(COMM.toString(), thread_id, description, details);
        }
        if (lvl == MESSAGE && log_message) {
            write(MESSAGE.toString(), thread_id, description, details);
        }
    }
