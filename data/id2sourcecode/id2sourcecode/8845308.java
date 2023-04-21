    public void perform(Session session, CommandArguments args, Log out) {
        int suspendPolicy = EventRequest.SUSPEND_ALL;
        String threadName = null;
        boolean onStart = true;
        boolean onDeath = true;
        while (args.hasMoreTokens()) {
            String arg = args.nextToken();
            if (arg.equals("start")) {
                onDeath = false;
            } else if (arg.equals("death")) {
                onStart = false;
            } else if (arg.equals("go")) {
                suspendPolicy = EventRequest.SUSPEND_NONE;
            } else if (arg.equals("thread")) {
                suspendPolicy = EventRequest.SUSPEND_EVENT_THREAD;
            } else {
                threadName = arg;
            }
        }
        BreakpointManager brkman = (BreakpointManager) session.getManager(BreakpointManager.class);
        Breakpoint bp = new ThreadBreakpoint(threadName, onStart, onDeath);
        try {
            brkman.addNewBreakpoint(bp);
            bp.setEnabled(false);
            bp.setSuspendPolicy(suspendPolicy);
            bp.setEnabled(true);
            out.writeln(Bundle.getString("threadbrk.breakpointAdded"));
        } catch (ResolveException re) {
            throw new CommandException(re);
        }
    }
