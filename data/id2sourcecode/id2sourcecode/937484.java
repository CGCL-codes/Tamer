    public boolean[] screen(String sequence, int seq_id) throws SQLException, Exception {
        id_seq = seq_id;
        String[] commands = { "mkdir", "analyse_results/" + data + "_" + id_test + "_" + id_seq };
        if (system == 1) {
            BufferedWriter bw = new BufferedWriter(new FileWriter("del.bat"));
            bw.write("cd analyse_results");
            bw.newLine();
            bw.write("md " + data + "_" + id_test + "_" + id_seq);
            bw.close();
            commands[0] = "del.bat";
            commands[1] = "";
        }
        Process child = Runtime.getRuntime().exec(commands);
        child.waitFor();
        String[] command = { "mkdir", "output/" + id_seq };
        if (system == 1) {
            BufferedWriter bw = new BufferedWriter(new FileWriter("del.bat"));
            bw.write("cd output");
            bw.newLine();
            bw.write("md " + id_seq);
            bw.close();
            command[0] = "del.bat";
            command[1] = "";
        }
        child = Runtime.getRuntime().exec(command);
        child.waitFor();
        child.getErrorStream().close();
        child.getInputStream().close();
        child.getOutputStream().close();
        child.destroy();
        boolean[] tab = { false, false };
        sequence = sequence.trim();
        sequence = sequence.toLowerCase();
        sequence = sequence.replaceAll("\\s", " ");
        Seq query = new Seq(Nature.DNA, sequence, div);
        if (query.getSymbolList().length() < 200) {
            System.out.println("DNA sequences smaller than 200 bp: non hit");
            return tab;
        }
        if ((version == 1 || version == 3)) {
            tab = parallel(query, seq_id);
        } else {
            tab = serial(query);
        }
        String[] command2 = { "sh", "-c", "rm -r -f output/" + id_seq };
        if (system == 1) {
            BufferedWriter bw = new BufferedWriter(new FileWriter("del.bat"));
            bw.write("cd output");
            bw.newLine();
            bw.write("rd " + id_seq);
            bw.close();
            command2[0] = "del.bat";
            command2[1] = "";
            command2[2] = "";
        }
        Process child2 = Runtime.getRuntime().exec(command2);
        child2.waitFor();
        child2.getErrorStream().close();
        child2.getInputStream().close();
        child2.getOutputStream().close();
        child2.destroy();
        return tab;
    }
