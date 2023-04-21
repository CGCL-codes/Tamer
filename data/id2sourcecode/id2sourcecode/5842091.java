    public void initializeVisualStateMachine(int errorNumber) {
        if (erroneousPaths.isEmpty()) return;
        if (choiceTrace != null) {
            choiceTrace.delete();
        }
        clearAnimation();
        BufferedWriter bw;
        try {
            choiceTrace = File.createTempFile("tempScript", ".es");
            choiceTrace.deleteOnExit();
            bw = new BufferedWriter(new FileWriter(choiceTrace));
        } catch (IOException e) {
            error("Problem writing to or creating script file.", e);
            return;
        }
        Scanner scan = new Scanner(erroneousPaths.get(errorNumber));
        try {
            for (String x = ""; scan.hasNextLine(); x = scan.nextLine()) bw.write(x + NEW_LINE);
            bw.close();
        } catch (IOException e) {
            error("Problem writing to script file.", e);
            return;
        }
        if (!stateMachine.hasMasterState()) {
            error("No state machine specified in JPF arguments.");
            return;
        }
        stateMachine = new VisualSimStateMachine(machineLog);
        Config errorConfig = JPF.createConfig(selectedConfig.getTargetArgParameters());
        errorConfig.setProperty("sc.script", choiceTrace.getPath());
        if (!stateMachine.initializeMachine(errorConfig)) {
            error("Problem initializing the StateMachine.");
        }
        choiceTrace.delete();
        updateStatus(Status.ERRORS_GENERATED);
        initDiagram();
    }
