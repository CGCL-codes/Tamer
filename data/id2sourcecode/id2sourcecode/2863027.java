    public void incomingTransferCompleted(IncomingHandle handle, long amountWritten, GridProcess gridProcess) {
        GridProcessOperations operations = gridProcess.getOperations();
        GetOperation getOperation = operations.getFinalPhaseOperation(handle);
        File received = new File(getOperation.getRemoteFilePath());
        File renamed = new File(getOperation.getLocalFilePath());
        try {
            FileUtils.copyFile(received, renamed);
            FileUtils.forceDelete(received);
        } catch (IOException e) {
            GridProcessError error = new GridProcessError(e, GridProcessErrorTypes.BROKER_ERROR);
            fail(error, gridProcess);
        }
        if (!getOperation.isTransferActive()) {
            getCollector().addData(new SchedulerData(getXmlCreator(LoggerXMLCreator.class).getXML(WorkerClientMessages.getRunningStateInvalidOperation("incomingTransferCompleted", STATE_NAME), LoggerXMLCreator.ERROR)));
            return;
        }
        getOperation.getGridResult().getGetOperationTransferTime(getOperation).setEndTime();
        gridProcess.incDataTransfered(amountWritten);
        operations.removeFinalPhaseOperation(handle);
        if (operations.areAllFinalPhaseOperationsFinished()) {
            gridProcess.getResult().setFinalPhaseEndTime();
            sabotageCheck(gridProcess);
            if (!gridProcess.getResult().wasSabotaged()) {
                finish(gridProcess);
            }
        }
    }
