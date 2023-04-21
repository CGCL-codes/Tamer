    public void perform(Session session, CommandArguments args, Log out) {
        if (!session.isActive()) {
            throw new CommandException(Bundle.getString("noActiveSession"));
        }
        ContextManager contextManager = (ContextManager) session.getManager(ContextManager.class);
        if (!args.hasMoreTokens()) {
            ThreadReference thread = contextManager.getCurrentThread();
            if (thread == null) {
                throw new CommandException(Bundle.getString("noCurrentThread"));
            } else {
                out.writeln(Bundle.getString("thread.currentThread") + ' ' + buildDescriptor(null, thread));
            }
            return;
        }
        VirtualMachine vm = session.getConnection().getVM();
        ThreadReference thread = Threads.getThreadByID(vm, args.nextToken());
        if (thread != null) {
            contextManager.setCurrentThread(thread);
        } else {
            throw new CommandException(Bundle.getString("invalidThreadID"));
        }
    }
