    public void run() {
        double diff, energy;
        ChannelRecord c1, c2;
        amRunning = true;
        while (keepOn) {
            if (latestCorrelation != null && latestCorrelation != activeCorrelation) {
                activeCorrelation = latestCorrelation;
                Collection<BPMPair> pairs = bpmController.selectedPairs.values();
                for (BPMPair pair : pairs) {
                    if (activeCorrelation.isCorrelated(pair.getChannel1().getId()) && activeCorrelation.isCorrelated(pair.getChannel2().getId())) {
                        c1 = (ChannelRecord) activeCorrelation.getRecord(pair.getChannel1().getId());
                        c2 = (ChannelRecord) activeCorrelation.getRecord(pair.getChannel2().getId());
                        diff = c2.doubleValue() - c1.doubleValue();
                        energyFinder.initCalc(pair.getLength(), pair.getWGuess());
                        energy = energyFinder.findEnergy(diff);
                        pair.energy = new Double(energy);
                        pair.stats.addSample(energy);
                    }
                }
                try {
                    bpmController.updateBPMTable();
                    Thread.currentThread().sleep(500);
                } catch (Exception ex) {
                    bpmController.dumpErr("Trouble sleeping in the BPM calculator");
                }
            }
        }
    }
