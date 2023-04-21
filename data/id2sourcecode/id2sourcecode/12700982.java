    protected void resumeThread(VirtualMachine vm, String tid, Log out) {
        ThreadReference thread = Threads.getThreadByID(vm, tid);
        if (thread != null) {
            thread.resume();
            out.writeln(Bundle.getString("resume.threadResumed"));
        } else {
            throw new CommandException(Bundle.getString("threadNotFound") + ' ' + tid);
        }
    }
