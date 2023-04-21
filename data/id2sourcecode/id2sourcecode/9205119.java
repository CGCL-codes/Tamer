    private void runPlanner() {
        try {
            Status(Status.PLANNING);
            String command = Config.get("planner");
            String arg = String.format("%s %s %s %d ", command, modelFile, inputPddlFile, numOfSolution);
            StdOut.write(null, arg + "\r\n");
            ProcessBuilder b = new ProcessBuilder(Constants.CMD_PATH, "/C", arg);
            b.redirectErrorStream(true);
            lpgProcess = b.start();
            InputStream is = lpgProcess.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) StdOut.write(null, line + "\r\n");
            StdOut.write(null, "Planner completed at %s.\r\n", new Date());
            Status(Status.READY);
            plannerThread = null;
            lpgProcess = null;
            stopPlanner();
        } catch (Exception e) {
            e.printStackTrace();
            Status(Status.READY);
        }
    }
