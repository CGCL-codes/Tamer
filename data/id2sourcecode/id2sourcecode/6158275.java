    public final void writeLog(Level lvl, long thread_id, String description, Throwable exception) {
        String detail = "";
        StackTraceElement[] stack_traces = exception.getStackTrace();
        for (int i = 0; i < stack_traces.length; i++) {
            detail += stack_traces[i].toString() + "\r";
        }
        writeLog(lvl, thread_id, description, exception.getMessage() + "\r" + detail);
    }
