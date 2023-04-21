    public void perform(Session session, CommandArguments args, Log out) {
        if (!session.isActive()) {
            throw new CommandException(Bundle.getString("noActiveSession"));
        }
        VirtualMachine vm = session.getVM();
        if (vm instanceof PathSearchingVirtualMachine) {
            PathSearchingVirtualMachine psvm = (PathSearchingVirtualMachine) vm;
            out.write(Bundle.getString("vminfo.basedir"));
            out.writeln("");
            out.writeln(psvm.baseDirectory());
            out.writeln("");
            List cpath = psvm.classPath();
            out.writeln(pathToString(Bundle.getString("vminfo.cpath"), cpath));
            out.writeln("");
            cpath = psvm.bootClassPath();
            out.writeln(pathToString(Bundle.getString("vminfo.bcpath"), cpath));
        }
        out.writeln("");
        out.write(Bundle.getString("vminfo.stratum"));
        out.write(" ");
        out.writeln(vm.getDefaultStratum());
        Preferences prefs = Preferences.userRoot().node("com/bluemarsh/jswat/util");
        int timeout = prefs.getInt("invocationTimeout", Defaults.INVOCATION_TIMEOUT);
        ContextManager ctxtman = (ContextManager) session.getManager(ContextManager.class);
        ThreadReference thread = ctxtman.getCurrentThread();
        if (thread == null) {
            out.writeln("");
            out.writeln(Bundle.getString("vminfo.nothread"));
            return;
        }
        List runtimeTypes = vm.classesByName("java.lang.Runtime");
        ReferenceType rtType = (ReferenceType) runtimeTypes.get(0);
        List methods = rtType.methodsByName("getRuntime", "()Ljava/lang/Runtime;");
        Method method = (Method) methods.get(0);
        List emptyList = new LinkedList();
        try {
            ObjectReference oref = (ObjectReference) Classes.invokeMethod(null, rtType, thread, method, emptyList, timeout);
            methods = rtType.methodsByName("availableProcessors", "()I");
            method = (Method) methods.get(0);
            Object rval = Classes.invokeMethod(oref, rtType, thread, method, emptyList, timeout);
            out.writeln("");
            out.write(Bundle.getString("vminfo.numprocs"));
            out.write(" ");
            out.writeln(rval.toString());
            methods = rtType.methodsByName("freeMemory", "()J");
            method = (Method) methods.get(0);
            rval = Classes.invokeMethod(oref, rtType, thread, method, emptyList, timeout);
            out.write(Bundle.getString("vminfo.freemem"));
            out.write(" ");
            out.writeln(rval.toString());
            methods = rtType.methodsByName("maxMemory", "()J");
            method = (Method) methods.get(0);
            rval = Classes.invokeMethod(oref, rtType, thread, method, emptyList, timeout);
            out.write(Bundle.getString("vminfo.maxmem"));
            out.write(" ");
            out.writeln(rval.toString());
            methods = rtType.methodsByName("totalMemory", "()J");
            method = (Method) methods.get(0);
            rval = Classes.invokeMethod(oref, rtType, thread, method, emptyList, timeout);
            out.write(Bundle.getString("vminfo.totalmem"));
            out.write(" ");
            out.writeln(rval.toString());
        } catch (Exception e) {
            throw new CommandException(e);
        }
    }
