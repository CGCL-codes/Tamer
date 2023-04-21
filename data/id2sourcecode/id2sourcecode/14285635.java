    private void calculateEnergy() {
        for (int i = 0; i < nFCTPairs; i++) {
            energies[i] = 0;
            Ns[i] = 0;
            FCTUps[i] = "";
            FCTDowns[i] = "";
            FCTUpScans[i] = false;
            FCTDownScans[i] = false;
            FCTUpOks[i] = false;
            FCTDownOks[i] = false;
        }
        indexFCT = -1;
        eneRefFCTPairBeforeDB1Raw = calcRefEnergyRaw(refFCTPairBeforeDB1, designEnergyRaw);
        nRefFCTPairBeforeDB1Raw = calcRefNWaveRaw(refFCTPairBeforeDB1, designEnergyRaw);
        eneRefFCTPairBetweenDB1DB2Raw = calcRefEnergyRaw(refFCTPairBetweenDB1DB2, designEnergyRaw);
        nRefFCTPairBetweenDB1DB2Raw = calcRefNWaveRaw(refFCTPairBetweenDB1DB2, designEnergyRaw);
        eneRefFCTPairAfterDB2Raw = calcRefEnergyRaw(refFCTPairAfterDB2, designEnergyRaw);
        nRefFCTPairAfterDB2Raw = calcRefNWaveRaw(refFCTPairAfterDB2, designEnergyRaw);
        if (caputFlag) {
            if (energyBeforeDB1ChannelRaw == null) {
                energyBeforeDB1ChannelRaw = ChannelFactory.defaultFactory().getChannel(prop.getEnergyBeforeDB1RecordRaw());
            }
            CaMonitorScalar.setChannel(energyBeforeDB1ChannelRaw, eneRefFCTPairBeforeDB1Raw);
            if (energyDB1DB2ChannelRaw == null) {
                energyDB1DB2ChannelRaw = ChannelFactory.defaultFactory().getChannel(prop.getEnergyDB1DB2RecordRaw());
            }
            CaMonitorScalar.setChannel(energyDB1DB2ChannelRaw, eneRefFCTPairBetweenDB1DB2Raw);
            if (energyAfterDB2ChannelRaw == null) {
                energyAfterDB2ChannelRaw = ChannelFactory.defaultFactory().getChannel(prop.getEnergyAfterDB2RecordRaw());
            }
            CaMonitorScalar.setChannel(energyAfterDB2ChannelRaw, eneRefFCTPairAfterDB2Raw);
        }
        if ((!isBeamOn) || (downstreamAccelerationRF == null)) {
            designEnergy = 0;
            if (caputFlag) {
                if (energyChannel == null) {
                    energyChannel = ChannelFactory.defaultFactory().getChannel(prop.getEnergyRecord());
                }
                eneBest = 0;
                CaMonitorScalar.setChannel(energyChannel, eneBest);
                if (energyBeforeDB1Channel == null) {
                    energyBeforeDB1Channel = ChannelFactory.defaultFactory().getChannel(prop.getEnergyBeforeDB1Record());
                }
                CaMonitorScalar.setChannel(energyBeforeDB1Channel, eneBest);
                if (energyBeforeDB1ChannelLong == null) {
                    energyBeforeDB1ChannelLong = ChannelFactory.defaultFactory().getChannel(prop.getEnergyBeforeDB1RecordLong());
                }
                CaMonitorScalar.setChannel(energyBeforeDB1Channel, eneBest);
                if (energyDB1DB2Channel == null) {
                    energyDB1DB2Channel = ChannelFactory.defaultFactory().getChannel(prop.getEnergyDB1DB2Record());
                }
                CaMonitorScalar.setChannel(energyDB1DB2Channel, eneBest);
                if (energyDB1DB2ChannelLong == null) {
                    energyDB1DB2ChannelLong = ChannelFactory.defaultFactory().getChannel(prop.getEnergyDB1DB2RecordLong());
                }
                CaMonitorScalar.setChannel(energyDB1DB2ChannelLong, eneBest);
                if (energyAfterDB2Channel == null) {
                    energyAfterDB2Channel = ChannelFactory.defaultFactory().getChannel(prop.getEnergyAfterDB2Record());
                }
                CaMonitorScalar.setChannel(energyAfterDB2Channel, eneBest);
                if (FCTPairChannel == null) {
                    FCTPairChannel = ChannelFactory.defaultFactory().getChannel(prop.getFCTPairRecord());
                }
                CaMonitorScalar.setChannel(FCTPairChannel, -1.);
            }
            return;
        }
        double sRF = downstreamAccelerationRF.getPosition();
        boolean energySet = false;
        FCTPairManager pairBest = null;
        if (scanRF == null) {
            designEnergy = downstreamAccelerationRF.getDesignEnergy();
            ArrayList<FCTPairManager> pairs = downstreamAccelerationRF.getFCTPairs();
            for (int ip = 0; ip < pairs.size(); ip++) {
                FCTPairManager pair = pairs.get(ip);
                FCTManager up = pair.getFCTUpstream();
                FCTManager down = pair.getFCTDownstream();
                FCTUps[ip] = up.getId();
                FCTDowns[ip] = down.getId();
                double sFCT = pair.getFCTUpstream().getPosition();
                pair.checkStatus();
                FCTUpScans[ip] = up.getIsScanned();
                FCTDownScans[ip] = down.getIsScanned();
                if (!up.getIsScanned() && up.getIsPhaseSwitchGood() && sFCT >= sRF && pair.getIsBeamOn()) {
                    FCTUpOks[ip] = true;
                }
                if (!down.getIsScanned() && down.getIsPhaseSwitchGood() && sFCT >= sRF && pair.getIsBeamOn()) {
                    FCTDownOks[ip] = true;
                }
                double ene = pair.calculateEnergy(adjustSwitch, designEnergy);
                energies[ip] = ene;
                Ns[ip] = pair.getN0();
                if ((ene > 0) && (!energySet)) {
                    pairBest = pair;
                    eneBest = ene;
                    energySet = true;
                    indexFCT = ip;
                }
            }
            boolean adjustRef = false;
            eneRefFCTPairBeforeDB1 = calcRefEnergy(refFCTPairBeforeDB1, sRF, adjustRef, designEnergy);
            nRefFCTPairBeforeDB1 = calcRefNWave(refFCTPairBeforeDB1, sRF, adjustRef, designEnergy);
            eneRefFCTPairBeforeDB1Long = calcRefEnergy(refFCTPairBeforeDB1Long, sRF, adjustRef, designEnergy);
            nRefFCTPairBeforeDB1Long = calcRefNWave(refFCTPairBeforeDB1Long, sRF, adjustRef, designEnergy);
            eneRefFCTPairBetweenDB1DB2 = calcRefEnergy(refFCTPairBetweenDB1DB2, sRF, adjustRef, designEnergy);
            nRefFCTPairBetweenDB1DB2 = calcRefNWave(refFCTPairBetweenDB1DB2, sRF, adjustRef, designEnergy);
            eneRefFCTPairBetweenDB1DB2Long = calcRefEnergy(refFCTPairBetweenDB1DB2Long, sRF, adjustRef, designEnergy);
            nRefFCTPairBetweenDB1DB2Long = calcRefNWave(refFCTPairBetweenDB1DB2Long, sRF, adjustRef, designEnergy);
            eneRefFCTPairAfterDB2 = calcRefEnergy(refFCTPairAfterDB2, sRF, adjustRef, designEnergy);
            nRefFCTPairAfterDB2 = calcRefNWave(refFCTPairAfterDB2, sRF, adjustRef, designEnergy);
            if (caputFlag) {
                if (energyChannel == null) {
                    energyChannel = ChannelFactory.defaultFactory().getChannel(prop.getEnergyRecord());
                }
                CaMonitorScalar.setChannel(energyChannel, eneBest);
                if (energyBeforeDB1Channel == null) {
                    energyBeforeDB1Channel = ChannelFactory.defaultFactory().getChannel(prop.getEnergyBeforeDB1Record());
                }
                CaMonitorScalar.setChannel(energyBeforeDB1Channel, eneRefFCTPairBeforeDB1);
                if (energyBeforeDB1ChannelLong == null) {
                    energyBeforeDB1ChannelLong = ChannelFactory.defaultFactory().getChannel(prop.getEnergyBeforeDB1RecordLong());
                }
                CaMonitorScalar.setChannel(energyBeforeDB1ChannelLong, eneRefFCTPairBeforeDB1Long);
                if (energyDB1DB2Channel == null) {
                    energyDB1DB2Channel = ChannelFactory.defaultFactory().getChannel(prop.getEnergyDB1DB2Record());
                }
                CaMonitorScalar.setChannel(energyDB1DB2Channel, eneRefFCTPairBetweenDB1DB2);
                if (energyDB1DB2ChannelLong == null) {
                    energyDB1DB2ChannelLong = ChannelFactory.defaultFactory().getChannel(prop.getEnergyDB1DB2RecordLong());
                }
                CaMonitorScalar.setChannel(energyDB1DB2ChannelLong, eneRefFCTPairBetweenDB1DB2Long);
                if (energyAfterDB2Channel == null) {
                    energyAfterDB2Channel = ChannelFactory.defaultFactory().getChannel(prop.getEnergyAfterDB2Record());
                }
                CaMonitorScalar.setChannel(energyAfterDB2Channel, eneRefFCTPairAfterDB2);
                if (FCTPairChannel == null) {
                    FCTPairChannel = ChannelFactory.defaultFactory().getChannel(prop.getFCTPairRecord());
                }
                int code = -1;
                if (pairBest != null) {
                    code = pairBest.getPairCode();
                }
                CaMonitorScalar.setChannel(FCTPairChannel, code);
            }
        } else {
            designEnergy = nextdownstreamAccelerationRF.getDesignEnergy();
            double sRFScan = scanRF.getPosition();
            ArrayList<FCTPairManager> pairs = downstreamAccelerationRF.getFCTPairs();
            for (int ip = 0; ip < pairs.size(); ip++) {
                FCTPairManager pair = pairs.get(ip);
                FCTManager up = pair.getFCTUpstream();
                FCTManager down = pair.getFCTDownstream();
                FCTUps[ip] = up.getId();
                FCTDowns[ip] = down.getId();
                FCTUpScans[ip] = up.getIsScanned();
                FCTDownScans[ip] = down.getIsScanned();
                double sFCT = pair.getFCTUpstream().getPosition();
                pair.checkStatus();
                if (!up.getIsScanned() && up.getIsPhaseSwitchGood() && sFCT >= sRFScan) {
                    FCTUpOks[ip] = true;
                }
                if (!down.getIsScanned() && down.getIsPhaseSwitchGood() && sFCT >= sRFScan) {
                    FCTDownOks[ip] = true;
                }
                double ene = pair.calculateEnergy(false, designEnergy);
                energies[ip] = ene;
                Ns[ip] = pair.getN0();
                if ((ene > 0) && (!energySet) && (sFCT > sRFScan)) {
                    pairBest = pair;
                    energySet = true;
                    indexFCT = ip;
                    eneBest = ene;
                }
            }
            boolean adjustRef = false;
            eneRefFCTPairBeforeDB1 = calcRefEnergy(refFCTPairBeforeDB1, sRFScan, adjustRef, designEnergy);
            nRefFCTPairBeforeDB1 = calcRefNWave(refFCTPairBeforeDB1, sRFScan, adjustRef, designEnergy);
            eneRefFCTPairBeforeDB1Long = calcRefEnergy(refFCTPairBeforeDB1Long, sRFScan, adjustRef, designEnergy);
            nRefFCTPairBeforeDB1Long = calcRefNWave(refFCTPairBeforeDB1Long, sRFScan, adjustRef, designEnergy);
            eneRefFCTPairBetweenDB1DB2 = calcRefEnergy(refFCTPairBetweenDB1DB2, sRFScan, adjustRef, designEnergy);
            nRefFCTPairBetweenDB1DB2 = calcRefNWave(refFCTPairBetweenDB1DB2, sRFScan, adjustRef, designEnergy);
            eneRefFCTPairBetweenDB1DB2Long = calcRefEnergy(refFCTPairBetweenDB1DB2Long, sRFScan, adjustRef, designEnergy);
            nRefFCTPairBetweenDB1DB2Long = calcRefNWave(refFCTPairBetweenDB1DB2Long, sRFScan, adjustRef, designEnergy);
            eneRefFCTPairAfterDB2 = calcRefEnergy(refFCTPairAfterDB2, sRFScan, adjustRef, designEnergy);
            nRefFCTPairAfterDB2 = calcRefNWave(refFCTPairAfterDB2, sRFScan, adjustRef, designEnergy);
            if (caputFlag) {
                if (energyChannel == null) {
                    energyChannel = ChannelFactory.defaultFactory().getChannel(prop.getEnergyRecord());
                }
                CaMonitorScalar.setChannel(energyChannel, eneBest);
                if (energyBeforeDB1Channel == null) {
                    energyBeforeDB1Channel = ChannelFactory.defaultFactory().getChannel(prop.getEnergyBeforeDB1Record());
                }
                CaMonitorScalar.setChannel(energyBeforeDB1Channel, eneRefFCTPairBeforeDB1);
                if (energyBeforeDB1ChannelLong == null) {
                    energyBeforeDB1ChannelLong = ChannelFactory.defaultFactory().getChannel(prop.getEnergyBeforeDB1RecordLong());
                }
                CaMonitorScalar.setChannel(energyBeforeDB1ChannelLong, eneRefFCTPairBeforeDB1Long);
                if (energyDB1DB2Channel == null) {
                    energyDB1DB2Channel = ChannelFactory.defaultFactory().getChannel(prop.getEnergyDB1DB2Record());
                }
                CaMonitorScalar.setChannel(energyDB1DB2Channel, eneRefFCTPairBetweenDB1DB2);
                if (energyDB1DB2ChannelLong == null) {
                    energyDB1DB2ChannelLong = ChannelFactory.defaultFactory().getChannel(prop.getEnergyDB1DB2RecordLong());
                }
                CaMonitorScalar.setChannel(energyDB1DB2Channel, eneRefFCTPairBetweenDB1DB2Long);
                if (energyAfterDB2Channel == null) {
                    energyAfterDB2Channel = ChannelFactory.defaultFactory().getChannel(prop.getEnergyAfterDB2Record());
                }
                CaMonitorScalar.setChannel(energyAfterDB2Channel, eneRefFCTPairAfterDB2);
                if (FCTPairChannel == null) {
                    FCTPairChannel = ChannelFactory.defaultFactory().getChannel(prop.getFCTPairRecord());
                }
                int code = -1;
                if (pairBest != null) {
                    code = pairBest.getPairCode();
                }
                CaMonitorScalar.setChannel(FCTPairChannel, code);
            }
        }
    }
