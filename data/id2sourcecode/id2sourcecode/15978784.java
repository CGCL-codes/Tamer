    public void save(String fileName) throws Exception {
        File file = new File(fileName);
        DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(file));
        outputStream.writeBytes("Experiment Data Output\n");
        outputStream.writeBytes("----------------------\n");
        ParameterEvent startEvent = ((TargetState) getTargetStates().get(0)).getStartEvent();
        long startTime = startEvent.getTimeStamp();
        ParameterEvent endEvent = ((TargetState) getTargetStates().get(getTargetStates().size() - 1)).getEndEvent();
        long endTime = endEvent.getTimeStamp();
        ExperimentSourceModule sourceModule = (ExperimentSourceModule) startEvent.getSourceModule();
        outputStream.writeBytes("Source: " + sourceModule.toString() + "\n");
        outputStream.writeBytes("Start Time: " + new Date(startTime) + " (" + startTime + "ms)\n");
        outputStream.writeBytes("End Time: " + new Date(endTime) + " (" + endTime + "ms)\n");
        outputStream.writeBytes("\n");
        List targetStates = getTargetStates();
        for (int i = 0; i < targetStates.size(); i++) {
            TargetState targetState = (TargetState) targetStates.get(i);
            long time = targetState.getStartEvent().getTimeStamp() - startTime;
            outputStream.writeBytes(time + " " + targetState.toString());
            outputStream.writeBytes("\n");
        }
        outputStream.writeBytes("\n");
        outputStream.writeBytes("Spreadsheet Format:\n");
        outputStream.writeBytes("State");
        for (int i = 0; i < targetStates.size(); i++) {
            TargetState targetState = (TargetState) targetStates.get(i);
            outputStream.writeBytes("," + targetState.toString());
        }
        outputStream.writeBytes("\n");
        outputStream.writeBytes("Duration [ms]");
        for (int i = 0; i < targetStates.size(); i++) {
            TargetState targetState = (TargetState) targetStates.get(i);
            long time0 = targetState.getStartEvent().getTimeStamp();
            long time1 = targetState.getEndEvent().getTimeStamp();
            long duration = time1 - time0;
            outputStream.writeBytes("," + duration);
        }
        outputStream.writeBytes("\n");
        outputStream.close();
    }
