    public void perform(CommandContext context, CommandArguments arguments) throws CommandException, MissingArgumentsException {
        Session session = context.getSession();
        PrintWriter writer = context.getWriter();
        VirtualMachine vm = session.getConnection().getVM();
        EventRequestManager erm = vm.eventRequestManager();
        List requests = erm.accessWatchpointRequests();
        Iterator iter = requests.iterator();
        writer.println("Access watchpoint requests:");
        while (iter.hasNext()) {
            AccessWatchpointRequest awr = (AccessWatchpointRequest) iter.next();
            printCommon(awr, writer);
            writer.print("\tField: ");
            writer.println(awr.field().toString());
            writer.println();
        }
        writer.println();
        requests = erm.breakpointRequests();
        iter = requests.iterator();
        writer.println("Breakpoint requests:");
        while (iter.hasNext()) {
            BreakpointRequest br = (BreakpointRequest) iter.next();
            printCommon(br, writer);
            writer.print("\tLocation: ");
            writer.println(br.location().toString());
            writer.println();
        }
        writer.println();
        requests = erm.classPrepareRequests();
        iter = requests.iterator();
        writer.println("Class prepare requests:");
        while (iter.hasNext()) {
            ClassPrepareRequest cpr = (ClassPrepareRequest) iter.next();
            printCommon(cpr, writer);
            writer.println();
        }
        writer.println();
        requests = erm.classUnloadRequests();
        iter = requests.iterator();
        writer.println("Class unload requests:");
        while (iter.hasNext()) {
            ClassUnloadRequest cur = (ClassUnloadRequest) iter.next();
            printCommon(cur, writer);
            writer.println();
        }
        writer.println();
        requests = erm.exceptionRequests();
        iter = requests.iterator();
        writer.println("Exception requests:");
        while (iter.hasNext()) {
            ExceptionRequest er = (ExceptionRequest) iter.next();
            printCommon(er, writer);
            writer.print("\tException: ");
            writer.println(String.valueOf(er.exception()));
            writer.print("\tNotify caught: ");
            writer.println(String.valueOf(er.notifyCaught()));
            writer.print("\tNotify uncaught: ");
            writer.println(String.valueOf(er.notifyUncaught()));
            writer.println();
        }
        writer.println();
        requests = erm.methodEntryRequests();
        iter = requests.iterator();
        writer.println("Method entry requests:");
        while (iter.hasNext()) {
            MethodEntryRequest mer = (MethodEntryRequest) iter.next();
            printCommon(mer, writer);
            writer.println();
        }
        writer.println();
        requests = erm.methodExitRequests();
        iter = requests.iterator();
        writer.println("Method exit requests:");
        while (iter.hasNext()) {
            MethodExitRequest mer = (MethodExitRequest) iter.next();
            printCommon(mer, writer);
            writer.println();
        }
        writer.println();
        requests = erm.modificationWatchpointRequests();
        iter = requests.iterator();
        writer.println("Modification watchpoint requests:");
        while (iter.hasNext()) {
            ModificationWatchpointRequest mwr = (ModificationWatchpointRequest) iter.next();
            printCommon(mwr, writer);
            writer.print("\tField: ");
            writer.println(mwr.field().toString());
            writer.println();
        }
        writer.println();
        requests = erm.stepRequests();
        iter = requests.iterator();
        writer.println("Step requests:");
        while (iter.hasNext()) {
            StepRequest sr = (StepRequest) iter.next();
            printCommon(sr, writer);
            writer.print("\tThread: ");
            if (sr.thread() != null) {
                writer.println(sr.thread().toString());
            }
            int depth = sr.depth();
            writer.print("\tDepth: ");
            if (depth == StepRequest.STEP_INTO) {
                writer.println("into");
            } else if (depth == StepRequest.STEP_OUT) {
                writer.println("out");
            } else if (depth == StepRequest.STEP_OVER) {
                writer.println("over");
            } else {
                writer.println("unknown");
            }
            int size = sr.size();
            writer.print("\tSize: ");
            if (size == StepRequest.STEP_MIN) {
                writer.println("instruction");
            } else if (size == StepRequest.STEP_LINE) {
                writer.println("line");
            } else {
                writer.println("unknown");
            }
            writer.println();
        }
        writer.println();
        requests = erm.threadDeathRequests();
        iter = requests.iterator();
        writer.println("Thread death requests:");
        while (iter.hasNext()) {
            ThreadDeathRequest tdr = (ThreadDeathRequest) iter.next();
            printCommon(tdr, writer);
            writer.println();
        }
        writer.println();
        requests = erm.threadStartRequests();
        iter = requests.iterator();
        writer.println("Thread start requests:");
        while (iter.hasNext()) {
            ThreadStartRequest tsr = (ThreadStartRequest) iter.next();
            printCommon(tsr, writer);
            writer.println();
        }
        writer.println();
        requests = erm.vmDeathRequests();
        iter = requests.iterator();
        writer.println("VM death requests:");
        while (iter.hasNext()) {
            VMDeathRequest vmdr = (VMDeathRequest) iter.next();
            printCommon(vmdr, writer);
            writer.println();
        }
        writer.println();
    }
