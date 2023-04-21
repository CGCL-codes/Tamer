    private synchronized void stopInCurrentSection(int task) {
        if (_currentAllocatedSection == null) {
            log.error("Current allocated section null on entry to stopInCurrentSection");
            setStopNow();
            return;
        }
        if ((_targetSpeed == 0.0f) || isStopping()) {
            return;
        }
        if (_currentAllocatedSection.getSection().getState() == Section.FORWARD) {
            _stopSensor = _currentAllocatedSection.getSection().getForwardStoppingSensor();
        } else {
            _stopSensor = _currentAllocatedSection.getSection().getReverseStoppingSensor();
        }
        if (_stopSensor != null) {
            if (_stopSensor.getKnownState() == Sensor.ACTIVE) {
                setStopNow();
            } else {
                setSpeed(RESTRICTED_SPEED);
                _stopSensor.addPropertyChangeListener(_stopSensorListener = new java.beans.PropertyChangeListener() {

                    public void propertyChange(java.beans.PropertyChangeEvent e) {
                        if (e.getPropertyName().equals("KnownState")) {
                            handleStopSensorChange();
                        }
                    }
                });
                _stoppingBySensor = true;
            }
        } else if (_currentAllocatedSection.getLength() < _maxTrainLength) {
            setStopNow();
        } else if (_resistanceWheels) {
            if (_currentAllocatedSection.getSection().getNumBlocks() == 1) {
                if ((_previousBlock != null) && (_previousBlock.getState() == Block.OCCUPIED)) {
                    _stoppingBlock = _previousBlock;
                    setStopByBlockOccupancy();
                } else {
                    setStopNow();
                }
            } else {
                Block exitBlock = _currentAllocatedSection.getExitBlock();
                Block enterBlock = _currentAllocatedSection.getEnterBlock(_previousAllocatedSection);
                if (exitBlock == null) {
                    Block testBlock = _currentAllocatedSection.getSection().getEntryBlock();
                    while ((testBlock != null) && (testBlock != enterBlock)) {
                        testBlock = _currentAllocatedSection.getSection().getNextBlock();
                    }
                    int testLength = getBlockLength(testBlock);
                    while (testBlock != null) {
                        testBlock = _currentAllocatedSection.getSection().getNextBlock();
                        if (testBlock != null) testLength += getBlockLength(testBlock);
                    }
                    if ((testLength > _maxTrainLength) && (_previousBlock != null) && (_previousBlock.getState() == Block.OCCUPIED)) {
                        _stoppingBlock = _previousBlock;
                        setStopByBlockOccupancy();
                    } else {
                        setStopNow();
                    }
                } else if (enterBlock == null) {
                    setStopNow();
                } else if (exitBlock == enterBlock) {
                    if ((_previousBlock != null) && (_previousBlock.getState() == Block.OCCUPIED) && (getBlockLength(exitBlock) > _maxTrainLength)) {
                        _stoppingBlock = _previousBlock;
                        setStopByBlockOccupancy();
                    } else {
                        setStopNow();
                    }
                } else {
                    int tstLength = getBlockLength(exitBlock);
                    Block tstBlock = exitBlock;
                    int tstBlockSeq = _currentAllocatedSection.getSection().getBlockSequenceNumber(tstBlock);
                    while ((tstLength < _maxTrainLength) && (tstBlock != enterBlock)) {
                        int newSeqNumber = tstBlockSeq - 1;
                        if (_currentAllocatedSection.getDirection() == Section.REVERSE) {
                            newSeqNumber = tstBlockSeq + 1;
                        }
                        tstBlock = _currentAllocatedSection.getSection().getBlockBySequenceNumber(newSeqNumber);
                        tstBlockSeq = newSeqNumber;
                        tstLength += getBlockLength(tstBlock);
                    }
                    if (tstLength < _maxTrainLength) {
                        setStopNow();
                    } else if (tstBlock == enterBlock) {
                        if ((_previousBlock != null) && (_previousBlock.getState() == Block.OCCUPIED)) {
                            _stoppingBlock = _previousBlock;
                            setStopByBlockOccupancy();
                        } else {
                            setStopNow();
                        }
                    } else {
                        int xSeqNumber = tstBlockSeq - 1;
                        if (_currentAllocatedSection.getDirection() == Section.REVERSE) {
                            xSeqNumber = tstBlockSeq + 1;
                        }
                        _stoppingBlock = _currentAllocatedSection.getSection().getBlockBySequenceNumber(xSeqNumber);
                        setStopByBlockOccupancy();
                    }
                }
            }
        } else {
            setStopNow();
        }
        if (task > 0) {
            Runnable waitForStop = new WaitForTrainToStop(task);
            Thread tWait = new Thread(waitForStop);
            tWait.start();
        }
    }
