    public void one_seq(String seq, int id_seq, int test, DatabaseReader read, String reg, int num, int system) throws Exception {
        ResultSet test_rset = read.read_Parameter(test);
        Screen s = new Screen(test_rset, conn, test, num, 5, system);
        long start = System.currentTimeMillis();
        seq = seq.toLowerCase();
        boolean[] hit = s.screen(seq, id_seq);
        long end = System.currentTimeMillis();
        write(reg, hit, (int) ((end - start) / 1000), test, id_seq, s.nb_subsequences);
        System.out.println("sequence screened " + id_seq);
        if (!hit[1] && !hit[0]) {
            String[] commands = { "sh", "-c", "rm -r -f analyse_results/5_" + test + "_" + id_seq };
            Process child = Runtime.getRuntime().exec(commands);
            child.waitFor();
            child.getErrorStream().close();
            child.getInputStream().close();
            child.getOutputStream().close();
            child.destroy();
        }
    }
