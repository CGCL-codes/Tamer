    @Override
    public void perform(CommandContext context, CommandArguments arguments) throws CommandException, MissingArgumentsException {
        Session session = context.getSession();
        PrintWriter writer = context.getWriter();
        VirtualMachine vm = session.getConnection().getVM();
        DebuggingContext dc = context.getDebuggingContext();
        ThreadReference current = dc.getThread();
        if (!arguments.hasMoreTokens()) {
            if (current == null) {
                throw new CommandException(getMessage("ERR_NoThread"));
            } else {
                printStack(current, writer, dc);
            }
        } else {
            arg = arguments.nextToken();
            if (arg.equals("all")) {
                List<ThreadReference> threads = vm.allThreads();
                for (ThreadReference thread : threads) {
                    printStack(thread, writer, dc);
                    writer.println();
                }
            } else {
                ThreadReference thread = Threads.findThread(vm, arg);
                if (thread != null) {
                    printStack(thread, writer, dc);
                } else {
                    throw new CommandException(getMessage("ERR_InvalidThreadID"));
                }
            }
        }
    }
