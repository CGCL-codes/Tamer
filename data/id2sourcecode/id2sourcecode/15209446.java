    @Override
    public void perform(CommandContext context, CommandArguments arguments) throws CommandException, MissingArgumentsException {
        Session session = context.getSession();
        PrintWriter writer = context.getWriter();
        VirtualMachine vm = session.getConnection().getVM();
        DebuggingContext dc = context.getDebuggingContext();
        ThreadReference current = dc.getThread();
        if (arguments.hasMoreTokens()) {
            long tid = -1;
            String name = arguments.nextToken();
            try {
                tid = Long.parseLong(name);
            } catch (NumberFormatException nfe) {
            }
            List<ThreadReference> threadsList = null;
            Iterator iter = Threads.iterateGroups(vm.topLevelThreadGroups());
            if (tid > -1) {
                while (iter.hasNext()) {
                    ThreadGroupReference group = (ThreadGroupReference) iter.next();
                    if (group.uniqueID() == tid) {
                        threadsList = group.threads();
                        break;
                    }
                }
            } else {
                threadsList = new ArrayList<ThreadReference>();
                Pattern patt = Pattern.compile(name, Pattern.CASE_INSENSITIVE);
                while (iter.hasNext()) {
                    ThreadGroupReference group = (ThreadGroupReference) iter.next();
                    Matcher matcher = patt.matcher(group.name());
                    if (matcher.find()) {
                        threadsList.addAll(group.threads());
                    } else {
                        String idstr = String.valueOf(group.uniqueID());
                        matcher = patt.matcher(idstr);
                        if (matcher.find()) {
                            threadsList.addAll(group.threads());
                        }
                    }
                }
            }
            if (threadsList == null || threadsList.size() == 0) {
                writer.println(NbBundle.getMessage(getClass(), "CTL_threads_noThreadsInGroup"));
            } else if (threadsList.size() > 0) {
                writer.println(printThreads(threadsList.iterator(), "  ", current));
            }
        } else {
            List topGroups = vm.topLevelThreadGroups();
            if (topGroups == null || topGroups.size() == 0) {
                writer.println(NbBundle.getMessage(getClass(), "CTL_threads_noThreads"));
            } else if (topGroups.size() > 0) {
                Iterator iter = topGroups.iterator();
                while (iter.hasNext()) {
                    ThreadGroupReference group = (ThreadGroupReference) iter.next();
                    printGroup(group, current, writer, "");
                }
            }
        }
    }
