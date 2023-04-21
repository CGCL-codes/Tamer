    private void add_cmd(String cmd, String rdr_name) {
        R_thread r_thread = (R_thread) r_threads.get(rdr_name);
        try {
            cmd = cmd + "\n";
            r_thread.get_pipe_out().write(cmd.getBytes());
            r_thread.get_pipe_out().flush();
        } catch (IOException e) {
            kill_r_thread(rdr_name);
        }
    }
