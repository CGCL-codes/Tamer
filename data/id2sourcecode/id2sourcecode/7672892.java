    public void perform(Session session, CommandArguments args, Log out) {
        if (!args.hasMoreTokens()) {
            BreakpointManager brkman = (BreakpointManager) session.getManager(BreakpointManager.class);
            Iterator iter = brkman.breakpoints(true);
            if (iter.hasNext()) {
                out.writeln(Bundle.getString("brkinfo.breakpointList"));
                while (iter.hasNext()) {
                    Breakpoint bp = (Breakpoint) iter.next();
                    printBreakpoint(bp, out);
                }
            } else {
                out.writeln(Bundle.getString("brkinfo.noBreakpointsDefined"));
            }
            return;
        }
        String brknumStr = args.nextToken();
        int brknum = -1;
        try {
            brknum = Integer.parseInt(brknumStr);
        } catch (NumberFormatException nfe) {
            throw new CommandException(Bundle.getString("brkinfo.badbrk"));
        }
        BreakpointManager brkman = (BreakpointManager) session.getManager(BreakpointManager.class);
        Breakpoint brk = brkman.getBreakpoint(brknum);
        if (brk == null) {
            throw new CommandException(Bundle.getString("brkinfo.nobrk"));
        }
        out.writeln(brk.toString());
        BreakpointGroup brkgrp = brk.getBreakpointGroup();
        out.write(Bundle.getString("brkinfo.brkgrp"));
        out.write(" ");
        out.writeln(brkgrp.getName());
        int expires = brk.getExpireCount();
        if (expires > 0) {
            out.write(Bundle.getString("brkinfo.expires"));
            out.write(" ");
            out.writeln(Integer.toString(expires));
        }
        int skips = brk.getSkipCount();
        if (skips > 0) {
            out.write(Bundle.getString("brkinfo.skips"));
            out.write(" ");
            out.writeln(Integer.toString(skips));
        }
        if (brk.isEnabled()) {
            out.writeln(Bundle.getString("brkinfo.enabled"));
        } else {
            out.writeln(Bundle.getString("brkinfo.disabled"));
        }
        if (brk.isResolved()) {
            out.writeln(Bundle.getString("brkinfo.resolved"));
        } else {
            out.writeln(Bundle.getString("brkinfo.unresolved"));
        }
        if (brk.hasExpired()) {
            out.writeln(Bundle.getString("brkinfo.expired"));
        }
        if (brk.isSkipping()) {
            out.writeln(Bundle.getString("brkinfo.skipping"));
        }
        String filters = brk.getClassFilters();
        if (filters != null && filters.length() > 0) {
            out.write(Bundle.getString("brkinfo.classFilters"));
            out.write(" ");
            out.writeln(filters);
        }
        filters = brk.getThreadFilters();
        if (filters != null && filters.length() > 0) {
            out.write(Bundle.getString("brkinfo.threadFilters"));
            out.write(" ");
            out.writeln(filters);
        }
        Iterator conditer = brk.conditions();
        if (conditer.hasNext()) {
            out.writeln(Bundle.getString("brkinfo.conditions"));
            while (conditer.hasNext()) {
                Condition cond = (Condition) conditer.next();
                out.writeln(cond.toString());
            }
        }
        Iterator moniter = brk.monitors();
        if (moniter.hasNext()) {
            out.writeln(Bundle.getString("brkinfo.monitors"));
            while (moniter.hasNext()) {
                Monitor mon = (Monitor) moniter.next();
                out.writeln(mon.toString());
            }
        }
    }
