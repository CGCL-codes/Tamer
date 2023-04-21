    private void putDiagPVsFromPVLogger() {
        HashMap bpmXMap = plds.getBPMXMap();
        HashMap bpmYMap = plds.getBPMYMap();
        HashMap bpmAmpMap = plds.getBPMAmpMap();
        HashMap bpmPhaseMap = plds.getBPMPhaseMap();
        Iterator ibpm = bpms.iterator();
        while (ibpm.hasNext()) {
            BPM bpm = (BPM) ibpm.next();
            Channel bpmX = bpm.getChannel(BPM.X_AVG_HANDLE);
            Channel bpmY = bpm.getChannel(BPM.Y_AVG_HANDLE);
            Channel bpmAmp = bpm.getChannel(BPM.AMP_AVG_HANDLE);
            try {
                System.out.println("Now updating " + bpm.getId());
                if (bpmXMap.containsKey(bpmX.getId())) bpmX.putVal(NoiseGenerator.setValForPV(((Double) bpmXMap.get(bpmX.getId())).doubleValue(), bpmNoise, bpmOffset));
                if (bpmYMap.containsKey(bpmY.getId())) bpmY.putVal(NoiseGenerator.setValForPV(((Double) bpmYMap.get(bpmY.getId())).doubleValue(), bpmNoise, bpmOffset));
                if (bpmAmpMap.containsKey(bpmAmp.getId())) bpmAmp.putVal(NoiseGenerator.setValForPV(((Double) bpmAmpMap.get(bpmAmp.getId())).doubleValue(), 5., 0.1));
                if (!(myProbe instanceof TransferMapProbe)) {
                    Channel bpmPhase = bpm.getChannel(BPM.PHASE_AVG_HANDLE);
                    if (bpmPhaseMap.containsKey(bpmPhase.getId())) bpmPhase.putVal(((Double) bpmPhaseMap.get(bpmPhase.getId())).doubleValue());
                }
            } catch (ConnectionException e) {
                System.out.println(e.getMessage());
            } catch (PutException e) {
                System.out.println(e.getMessage());
            }
        }
    }
