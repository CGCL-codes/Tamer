    @Override
    public void perform(CommandContext context, CommandArguments arguments) throws CommandException, MissingArgumentsException {
        Session session = context.getSession();
        VirtualMachine vm = session.getConnection().getVM();
        PrintWriter writer = context.getWriter();
        DebuggingContext dc = ContextProvider.getContext(session);
        String token = arguments.nextToken();
        ThreadReference thread = Threads.findThread(vm, token);
        if (thread == null) {
            throw new CommandException(NbBundle.getMessage(ThreadKillCommand.class, "ERR_ThreadNotFound", token));
        }
        if (!arguments.hasMoreTokens()) {
            throw new MissingArgumentsException();
        }
        arguments.returnAsIs(true);
        Evaluator eval = new Evaluator(arguments.rest());
        ObjectReference object = null;
        try {
            Object o = eval.evaluate(thread, dc.getFrame());
            if (o instanceof ObjectReference) {
                object = (ObjectReference) o;
            } else {
                throw new CommandException(NbBundle.getMessage(getClass(), "ERR_ExprNotAnObject"));
            }
        } catch (EvaluationException ee) {
            Throwable t = ee.getCause();
            if (t instanceof ClassNotPreparedException) {
                throw new CommandException(NbBundle.getMessage(getClass(), "ERR_ClassNotPrepared"), t);
            } else if (t instanceof IllegalThreadStateException) {
                throw new CommandException(NbBundle.getMessage(getClass(), "ERR_ThreadNoStack"), t);
            } else if (t instanceof IndexOutOfBoundsException) {
                throw new CommandException(NbBundle.getMessage(getClass(), "ERR_InvalidStackFrame"), t);
            } else if (t instanceof InvalidStackFrameException) {
                throw new CommandException(NbBundle.getMessage(getClass(), "ERR_InvalidStackFrame"), t);
            } else if (t instanceof NativeMethodException) {
                throw new CommandException(NbBundle.getMessage(getClass(), "ERR_NativeMethod"), t);
            } else if (t instanceof ObjectCollectedException) {
                throw new CommandException(NbBundle.getMessage(getClass(), "ERR_ObjectCollected"), t);
            } else {
                throw new CommandException(NbBundle.getMessage(getClass(), "ERR_EvaluationError", ee.getMessage()), ee);
            }
        }
        try {
            thread.stop(object);
        } catch (InvalidTypeException ite) {
            throw new CommandException(NbBundle.getMessage(getClass(), "ERR_kill_NotThrowable"), ite);
        }
        writer.println(NbBundle.getMessage(ThreadKillCommand.class, "CTL_kill_Signaled", thread.uniqueID()));
    }
