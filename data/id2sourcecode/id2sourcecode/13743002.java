    public void run() {
        List<Run> runs = theLab.getConfig().getRunConfiguration().getRuns();
        List<RunDefinitionAnalyzed> analysedRuns = new ArrayList<RunDefinitionAnalyzed>();
        HashMap<Integer, RunExecutionHandler> handlerPerRun = new HashMap<Integer, RunExecutionHandler>();
        int runCount = runs.size();
        int finishedRuns = 0;
        int[] runIndices = new int[runCount];
        for (int i = 0; i < runCount; ++i) {
            runIndices[i] = 0;
            analysedRuns.add(theLab.getRunSetsPerRunDefinition().get(runs.get(i).getName()));
        }
        long executionStartTime = System.currentTimeMillis();
        while (doContinue) {
            long cycleStartTime = System.currentTimeMillis();
            long relativeTime = cycleStartTime - executionStartTime;
            for (int i = 0; i < runCount; ++i) {
                RunExecutionHandler handler = handlerPerRun.get(i);
                Run currRun = runs.get(i);
                if (handler != null) {
                    boolean completed = handler.process(relativeTime);
                    sendValueToBoard(analysedRuns.get(i).boardType, currRun.getChannel(), currRun.getAddress(), analysedRuns.get(i).getParameter(), handler.getCurrentValue());
                    if (completed) handlerPerRun.remove(i);
                } else {
                    if (analysedRuns.get(i).getRunSets().size() > runIndices[i]) {
                        RunSetAnalyzed set = (RunSetAnalyzed) analysedRuns.get(i).getRunSets().get(runIndices[i]);
                        if (relativeTime >= set.getTimestamp()) {
                            if (set.getForm() == RunSet.FORM.LINEAR) {
                                RunSetAnalyzed previousSet = (RunSetAnalyzed) analysedRuns.get(i).getRunSets().get(runIndices[i] - 1);
                                LinearRunHandler newHandler = new LinearRunHandler(previousSet.getValue(), set.getValue(), set.getFormDuration(), set.getForm(), theLab, relativeTime, precision);
                                handlerPerRun.put(i, newHandler);
                                boolean completed = newHandler.process(relativeTime);
                                sendValueToBoard(analysedRuns.get(i).boardType, currRun.getChannel(), currRun.getAddress(), analysedRuns.get(i).getParameter(), newHandler.getCurrentValue());
                                if (completed) handlerPerRun.remove(i);
                            } else {
                                sendValueToBoard(analysedRuns.get(i).boardType, currRun.getChannel(), currRun.getAddress(), analysedRuns.get(i).getParameter(), set.getValue());
                            }
                            runIndices[i]++;
                            if (analysedRuns.get(i).getRunSets().size() == runIndices[i]) {
                                finishedRuns++;
                                if (finishedRuns == runCount) {
                                    if (callback != null) callback.runFinished();
                                    return;
                                }
                            }
                        }
                    }
                }
            }
            long processingTime = System.currentTimeMillis() - cycleStartTime;
            if (processingTime < precision) {
                try {
                    Thread.sleep(precision - processingTime);
                } catch (InterruptedException e) {
                }
            } else {
                System.err.println("Precision problem");
            }
        }
    }
