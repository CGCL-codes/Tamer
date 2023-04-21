    public Double classifyRecord(List<Object> record, Integer targetAttribute) {
        try {
            Process p = Runtime.getRuntime().exec("c:\\F\\devel\\ilp\\SWI\\bin\\plcon.exe", null, new File("c:\\install\\ilp\\"));
            BufferedReader stdOut = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdErr = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            PrintStream stdIn = new PrintStream(new BufferedOutputStream(p.getOutputStream()), true);
            stdIn.write("consult('c:/install/ilp/aleph.pl').\n".getBytes());
            stdIn.flush();
            read(stdOut, stdErr, "true.");
            stdIn.write("read_all(train).\n".getBytes());
            stdIn.flush();
            read(stdOut, stdErr, "true");
            stdIn.write(".\n".getBytes());
            stdIn.flush();
            read(stdOut, stdErr, "");
            stdIn.write("induce.\n".getBytes());
            stdIn.flush();
            read(stdOut, stdErr, "true.");
            stdIn.write("eastbound(west7).\n".getBytes());
            stdIn.flush();
            @SuppressWarnings("unused") String res = getResult(stdOut, stdErr);
            return 0.0D;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0D;
    }
