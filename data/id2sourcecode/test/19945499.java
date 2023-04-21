    protected void runPlanner() {
        try {
            String tempFolder = config.getString("temp_folder", "");
            int i = modelFile.lastIndexOf(".");
            inputPddlFile = tempFolder + "\\" + modelFile.substring(0, i) + "-auto.pddl";
            problem = model.generatePddlProblem();
            problem.writeToFile(inputPddlFile);
            setStatus(Status.PLANNING);
            String command = config.getString("planner", "");
            if (command.length() > 0) {
                String arg = String.format("%s %s %s %d %d", command, modelFile, inputPddlFile, getMaxSolution(), getMaxCpuTime());
                writeMessage(arg + "\r\n");
                ProcessBuilder b = new ProcessBuilder(Constants.CMD_PATH, "/C", arg);
                b.redirectErrorStream(true);
                lpgProcess = b.start();
                InputStream is = lpgProcess.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line = br.readLine();
                cancelled = false;
                for (; line != null && !this.cancelled; ) while ((line = br.readLine()) != null) writeMessage(line + "\r\n");
                if (!this.cancelled) writeMessage("Planner completed at %s.\r\n", new Date()); else {
                    writeMessage("User cancelled as %s.\r\n", new Date());
                    lpgProcess.destroy();
                }
                setStatus(Status.READY);
                lpgProcess = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            setStatus(Status.READY);
        }
    }
