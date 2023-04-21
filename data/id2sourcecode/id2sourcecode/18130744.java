    public void perform(Session session, CommandArguments args, Log out) {
        if (!session.isActive()) {
            throw new CommandException("Must have active session for 'requests'.");
        }
        VirtualMachine vm = session.getVM();
        EventRequestManager erm = vm.eventRequestManager();
        List requests = erm.accessWatchpointRequests();
        Iterator iter = requests.iterator();
        out.writeln("Access watchpoint requests:");
        while (iter.hasNext()) {
            AccessWatchpointRequest awr = (AccessWatchpointRequest) iter.next();
            printBasics(awr, out);
            out.write("\tField: ");
            out.writeln(awr.field().toString());
            out.writeln("");
        }
        out.writeln("");
        requests = erm.breakpointRequests();
        iter = requests.iterator();
        out.writeln("Breakpoint requests:");
        while (iter.hasNext()) {
            BreakpointRequest br = (BreakpointRequest) iter.next();
            printBasics(br, out);
            out.write("\tLocation: ");
            out.writeln(br.location().toString());
            out.writeln("");
        }
        out.writeln("");
        requests = erm.classPrepareRequests();
        iter = requests.iterator();
        out.writeln("Class prepare requests:");
        while (iter.hasNext()) {
            ClassPrepareRequest cpr = (ClassPrepareRequest) iter.next();
            printBasics(cpr, out);
            out.writeln("");
        }
        out.writeln("");
        requests = erm.classUnloadRequests();
        iter = requests.iterator();
        out.writeln("Class unload requests:");
        while (iter.hasNext()) {
            ClassUnloadRequest cur = (ClassUnloadRequest) iter.next();
            printBasics(cur, out);
            out.writeln("");
        }
        out.writeln("");
        requests = erm.exceptionRequests();
        iter = requests.iterator();
        out.writeln("Exception requests:");
        while (iter.hasNext()) {
            ExceptionRequest er = (ExceptionRequest) iter.next();
            printBasics(er, out);
            out.write("\tException: ");
            out.writeln(String.valueOf(er.exception()));
            out.write("\tNotify caught: ");
            out.writeln(String.valueOf(er.notifyCaught()));
            out.write("\tNotify uncaught: ");
            out.writeln(String.valueOf(er.notifyUncaught()));
            out.writeln("");
        }
        out.writeln("");
        requests = erm.methodEntryRequests();
        iter = requests.iterator();
        out.writeln("Method entry requests:");
        while (iter.hasNext()) {
            MethodEntryRequest mer = (MethodEntryRequest) iter.next();
            printBasics(mer, out);
            out.writeln("");
        }
        out.writeln("");
        requests = erm.methodExitRequests();
        iter = requests.iterator();
        out.writeln("Method exit requests:");
        while (iter.hasNext()) {
            MethodExitRequest mer = (MethodExitRequest) iter.next();
            printBasics(mer, out);
            out.writeln("");
        }
        out.writeln("");
        requests = erm.modificationWatchpointRequests();
        iter = requests.iterator();
        out.writeln("Modification watchpoint requests:");
        while (iter.hasNext()) {
            ModificationWatchpointRequest mwr = (ModificationWatchpointRequest) iter.next();
            printBasics(mwr, out);
            out.write("\tField: ");
            out.writeln(mwr.field().toString());
            out.writeln("");
        }
        out.writeln("");
        requests = erm.stepRequests();
        iter = requests.iterator();
        out.writeln("Step requests:");
        while (iter.hasNext()) {
            StepRequest sr = (StepRequest) iter.next();
            printBasics(sr, out);
            out.write("\tThread: ");
            if (sr.thread() != null) {
                out.writeln(sr.thread().toString());
            }
            int depth = sr.depth();
            out.write("\tDepth: ");
            if (depth == StepRequest.STEP_INTO) {
                out.writeln("into");
            } else if (depth == StepRequest.STEP_OUT) {
                out.writeln("out");
            } else if (depth == StepRequest.STEP_OVER) {
                out.writeln("over");
            } else {
                out.writeln("unknown");
            }
            int size = sr.size();
            out.write("\tSize: ");
            if (depth == StepRequest.STEP_MIN) {
                out.writeln("instruction");
            } else if (depth == StepRequest.STEP_LINE) {
                out.writeln("line");
            } else {
                out.writeln("unknown");
            }
            out.writeln("");
        }
        out.writeln("");
        requests = erm.threadDeathRequests();
        iter = requests.iterator();
        out.writeln("Thread death requests:");
        while (iter.hasNext()) {
            ThreadDeathRequest tdr = (ThreadDeathRequest) iter.next();
            printBasics(tdr, out);
            out.writeln("");
        }
        out.writeln("");
        requests = erm.threadStartRequests();
        iter = requests.iterator();
        out.writeln("Thread start requests:");
        while (iter.hasNext()) {
            ThreadStartRequest tsr = (ThreadStartRequest) iter.next();
            printBasics(tsr, out);
            out.writeln("");
        }
        out.writeln("");
        requests = erm.vmDeathRequests();
        iter = requests.iterator();
        out.writeln("VM death requests:");
        while (iter.hasNext()) {
            VMDeathRequest vmdr = (VMDeathRequest) iter.next();
            printBasics(vmdr, out);
            out.writeln("");
        }
        out.writeln("");
    }
