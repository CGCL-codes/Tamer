    protected void printBasics(EventRequest er, Log out) {
        out.write("\tEnabled: ");
        out.writeln(String.valueOf(er.isEnabled()));
        out.write("\tSuspend policy: ");
        int policy = er.suspendPolicy();
        if (policy == EventRequest.SUSPEND_ALL) {
            out.writeln("all");
        } else if (policy == EventRequest.SUSPEND_EVENT_THREAD) {
            out.writeln("thread");
        } else if (policy == EventRequest.SUSPEND_NONE) {
            out.writeln("none");
        } else {
            out.writeln("unknown");
        }
    }
