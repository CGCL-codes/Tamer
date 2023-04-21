    @Override
    public void update() {
        excCommandList.clear();
        inhCommandList.clear();
        final int size = mail.size();
        for (int i = 0; i < size; i++) {
            final IntFireMessage intFireMessage = mail.get(i);
            final IntFireMessage.Type type = intFireMessage.getType();
            switch(type) {
                case EXCITATORY_INJECTOR_OFF_REQUEST:
                    excCommandList.add(ExpDecayInjector.Command.OFF);
                    break;
                case EXCITATORY_INJECTOR_ON_REQUEST:
                    excCommandList.add(ExpDecayInjector.Command.ON);
                    break;
                case INHIBITORY_INJECTOR_OFF_REQUEST:
                    inhCommandList.add(ExpDecayInjector.Command.OFF);
                    break;
                case INHIBITORY_INJECTOR_ON_REQUEST:
                    inhCommandList.add(ExpDecayInjector.Command.ON);
                    break;
                case TOGGLE_PAUSE_REQUEST:
                    simulationRunning = !simulationRunning;
                    break;
                default:
            }
        }
        if (simulationRunning) {
            boolean spiked = false;
            spikeCount = 0;
            for (int i = 0; i < UPDATES_PER_UPDATE; i++) {
                for (final ThreePhaseUpdate threePhaseUpdate : threePhaseUpdates) {
                    threePhaseUpdate.access();
                }
                for (final ThreePhaseUpdate threePhaseUpdate : threePhaseUpdates) {
                    threePhaseUpdate.digest();
                }
                for (final ThreePhaseUpdate threePhaseUpdate : threePhaseUpdates) {
                    threePhaseUpdate.mutate();
                }
                SimLib.update(intFireNeuronImp);
                if (intFireNeuronImp.isSpiking()) {
                    spiked = true;
                    spikeCount++;
                }
            }
            final double membraneVoltage = intFireNeuronImp.getMembraneVoltage();
            final double absMembraneVoltage = Math.abs(membraneVoltage);
            if (absMembraneVoltage > membraneVoltageRange) {
                membraneVoltageRange = absMembraneVoltage;
            }
            membraneVoltageTimeSeries[offset] = membraneVoltage;
            spikeTimeSeries[offset] = spiked;
            double excitatoryCurrent = 0, inhibitoryCurrent = 0;
            final int injectorSeqSize = injectorSeq.size();
            for (int i = 0; i < injectorSeqSize; i++) {
                final Injector injector = injectorSeq.get(i);
                if (!injector.isOn()) {
                    continue;
                }
                final double current = injector.getCurrent();
                if (current > 0) {
                    inhibitoryCurrent += current;
                    if (current > inhibitoryCurrentRange) {
                        inhibitoryCurrentRange = current;
                    }
                } else {
                    excitatoryCurrent += current;
                    final double absCurrent = Math.abs(current);
                    if (absCurrent > excitatoryCurrentRange) {
                        excitatoryCurrentRange = absCurrent;
                    }
                }
            }
            excitatoryCurrentTimeSeries[offset] = excitatoryCurrent;
            inhibitoryCurrentTimeSeries[offset] = inhibitoryCurrent;
            offset = (offset + 1) % timeSeriesLength;
            timeStep++;
            timeMin = (timeStep - timeSeriesLength) * TIME_INTERVAL;
        }
    }
