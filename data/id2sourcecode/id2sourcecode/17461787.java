    public void beginRun() throws JamException, SortException {
        try {
            RunInfo.getInstance().runNumber = Integer.parseInt(tRunNumber.getText().trim());
            RunInfo.getInstance().runTitle = textRunTitle.getText().trim();
            RunInfo.getInstance().runStartTime = new Date();
        } catch (NumberFormatException nfe) {
            throw new JamException("Run number not an integer.", nfe);
        }
        if (device == Device.DISK) {
            final String EVENT_EXT = ".evn";
            final String dataFileName = RunInfo.getInstance().experimentName + RunInfo.getInstance().runNumber + EVENT_EXT;
            final File dataFile = new File(dataPath, dataFileName);
            if (dataFile.exists()) {
                throw new JamException("Event file already exits, File: " + dataFile.getPath() + ", Jam Cannot overwrite. [RunControl]");
            }
            diskDaemon.openEventOutputFile(dataFile);
            diskDaemon.writeHeader();
        }
        sortDaemon.userBegin();
        if (cHistZero.isSelected()) {
            jam.data.AbstractHistogram.setZeroAll();
        }
        if (zeroScalers.isSelected()) {
            scaler.clearScalers();
        }
        if (device != Device.FRONT_END) {
            netDaemon.setWriter(true);
        }
        end.setEnabled(true);
        begin.setEnabled(false);
        setLockControls(true);
        status.setRunState(RunState.runOnline(RunInfo.getInstance().runNumber));
        if (device == Device.DISK) {
            LOGGER.info("Began run " + RunInfo.getInstance().runNumber + ", events being written to file: " + diskDaemon.getEventOutputFile().getPath());
        } else {
            LOGGER.info("Began run, events being written out be front end.");
        }
        setRunOn(true);
        netDaemon.setEmptyBefore(false);
        netDaemon.setState(State.RUN);
        frontEnd.startAcquisition();
    }
