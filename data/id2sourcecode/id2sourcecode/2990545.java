    protected void suspendThread(VirtualMachine vm, String tid, Log out) {
        ThreadReference thread = Threads.getThreadByID(vm, tid);
        if (thread != null) {
            thread.suspend();
            out.writeln(Bundle.getString("suspend.threadSuspended"));
        } else {
            throw new CommandException(Bundle.getString("threadNotFound") + ' ' + tid);
        }
    }
