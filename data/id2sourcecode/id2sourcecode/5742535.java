    protected void putDiagPVs() {
        Iterator ibpm = bpms.iterator();
        while (ibpm.hasNext()) {
            BPM bpm = (BPM) ibpm.next();
            Channel bpmX = bpm.getChannel(BPM.X_AVG_HANDLE);
            Channel bpmY = bpm.getChannel(BPM.Y_AVG_HANDLE);
            Channel bpmAmp = bpm.getChannel(BPM.AMP_AVG_HANDLE);
            Channel bpmPhase = bpm.getChannel(BPM.PHASE_AVG_HANDLE);
            try {
                ProbeState probeState = model.getTrajectory().stateForElement(bpm.getId());
                System.out.println("Now updating " + bpm.getId());
                if (probeState instanceof ICoordinateState) {
                    final PhaseVector coordinates = ((ICoordinateState) probeState).getFixedOrbit();
                    bpmX.putVal(NoiseGenerator.setValForPV(coordinates.getx() * 1000., bpmNoise, bpmOffset));
                    bpmY.putVal(NoiseGenerator.setValForPV(coordinates.gety() * 1000., bpmNoise, bpmOffset));
                }
                bpmAmp.putVal(NoiseGenerator.setValForPV(20., 5., 0.1));
                bpmPhase.putVal(probeState.getTime() * 360. * (((BPMBucket) bpm.getBucket("bpm")).getFrequency() * 1.e6) % 360.0);
            } catch (ConnectionException e) {
                System.out.println(e.getMessage());
            } catch (PutException e) {
                System.out.println(e.getMessage());
            }
        }
        Iterator iws = wss.iterator();
        while (iws.hasNext()) {
            ProfileMonitor ws = (ProfileMonitor) iws.next();
            Channel wsX = ws.getChannel(ProfileMonitor.H_SIGMA_M_HANDLE);
            Channel wsY = ws.getChannel(ProfileMonitor.V_SIGMA_M_HANDLE);
            try {
                ProbeState probeState = model.getTrajectory().stateForElement(ws.getId());
                System.out.println("Now updating " + ws.getId());
                if (model.getProbe() instanceof EnvelopeProbe) {
                    wsX.putVal(((EnvelopeProbeState) probeState).getCorrelationMatrix().getSigmaX() * 1000.);
                    wsY.putVal(((EnvelopeProbeState) probeState).getCorrelationMatrix().getSigmaY() * 1000.);
                }
            } catch (ConnectionException e) {
                System.out.println(e.getMessage());
            } catch (PutException e) {
                System.out.println(e.getMessage());
            }
        }
    }
