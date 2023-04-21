    private static void display_r_threads(Iterator it, Screen screen) {
        R_thread r_thread;
        Iterator cmd;
        Iterator ret;
        Integer r;
        screen.writeln("");
        while (it.hasNext()) {
            r_thread = (R_thread) it.next();
            screen.writeln("[#" + r_thread.get_id() + " " + R_thread_status.get_statusMsg(r_thread.get_status()) + "  --- " + r_thread.get_rdr_name() + "] ");
            cmd = r_thread.get_cmds().iterator();
            ret = r_thread.get_ret_values().iterator();
            while (cmd.hasNext() && ret.hasNext()) {
                r = (Integer) ret.next();
                screen.writeln(cmd.next() + " :" + Errors.get_errorMsg(r.intValue()) + "(" + r.intValue() + ")");
            }
            screen.writeln("");
        }
        screen.writeln("");
    }
