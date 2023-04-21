    public void run() {
        runInit();
        SpectStreamSlot runInSlot;
        SpectStreamSlot runOutSlot;
        SpectStream runInStream = null;
        SpectStream runOutStream = null;
        SpectFrame runInFr = null;
        SpectFrame runOutFr = null;
        Param ampRef = new Param(1.0, Param.ABS_AMP);
        double gain;
        int ch2;
        boolean recalc = true;
        float phaseOffset = 0.0f;
        float foo;
        int loBand;
        Param freq;
        Param freqFloorOffs;
        Param freqCeilOffs;
        Param freqFloor;
        Param freqCeil;
        float depthFactor[];
        float depth[];
        float freqPhase[];
        double val;
        topLevel: try {
            runInSlot = (SpectStreamSlot) slots.elementAt(SLOT_INPUT);
            if (runInSlot.getLinked() == null) {
                runStop();
            }
            for (boolean initDone = false; !initDone && !threadDead; ) {
                try {
                    runInStream = runInSlot.getDescr();
                    initDone = true;
                } catch (InterruptedException e) {
                }
                runCheckPause();
            }
            if (threadDead) break topLevel;
            depthFactor = new float[runInStream.bands];
            depth = new float[runInStream.bands];
            freqPhase = new float[runInStream.bands];
            ch2 = 1 % runInStream.chanNum;
            runOutSlot = (SpectStreamSlot) slots.elementAt(SLOT_OUTPUT);
            runOutStream = new SpectStream(runInStream);
            runOutStream.setChannels(2);
            runOutSlot.initWriter(runOutStream);
            gain = (float) Param.transform(pr.para[PR_GAIN], Param.ABS_AMP, ampRef, runInStream).val;
            freq = new Param(0.0, Param.ABS_HZ);
            freqCeilOffs = pr.para[PR_BANDWIDTH];
            freqFloorOffs = new Param(-freqCeilOffs.val, freqCeilOffs.unit);
            loBand = 1;
            for (int i = loBand; i < runInStream.bands; i++) {
                freq.val = i * runInStream.hiFreq / (runInStream.bands - 1);
                freqCeil = Param.transform(freqCeilOffs, Param.ABS_HZ, freq, runInStream);
                freqFloor = Param.transform(freqFloorOffs, Param.ABS_HZ, freq, runInStream);
                freqPhase[i] = (float) ((freq.val / (freqCeil.val - freqFloor.val)) % 1.0);
                depthFactor[i] = (float) (pr.para[PR_LODEPTH].val + ((pr.para[PR_HIDEPTH].val - pr.para[PR_LODEPTH].val) * freq.val / hiFreq));
            }
            runSlotsReady();
            mainLoop: while (!threadDead) {
                if (pr.bool[PR_PHASEMOD]) {
                    foo = (float) (pr.para[PR_PHASEMODFREQ].val * ((runInStream.getTime() / 1000.0) % (1.0 / pr.para[PR_PHASEMODFREQ].val)));
                    if (Math.abs(foo - phaseOffset) >= 0.01) {
                        phaseOffset = foo;
                        recalc = true;
                    }
                }
                for (boolean readDone = false; (readDone == false) && !threadDead; ) {
                    try {
                        runInFr = runInSlot.readFrame();
                        readDone = true;
                        runOutFr = runOutStream.allocFrame();
                    } catch (InterruptedException e) {
                    } catch (EOFException e) {
                        break mainLoop;
                    }
                    runCheckPause();
                }
                if (threadDead) break mainLoop;
                if (recalc) {
                    for (int i = loBand; i < runInStream.bands; i++) {
                        val = Math.sin(Math.PI * (freqPhase[i] + phaseOffset));
                        val = (val * val - 0.5) * depthFactor[i];
                        depth[i] = (float) Math.exp(val / 20 * Constants.ln10);
                    }
                    recalc = false;
                }
                System.arraycopy(runInFr.data[0], 0, runOutFr.data[0], 0, runInFr.data[0].length);
                System.arraycopy(runInFr.data[ch2], 0, runOutFr.data[1], 0, runInFr.data[ch2].length);
                for (int band = loBand; band < runOutStream.bands; band++) {
                    runOutFr.data[0][(band << 1) + SpectFrame.AMP] *= gain * depth[band];
                    runOutFr.data[1][(band << 1) + SpectFrame.AMP] *= gain / depth[band];
                }
                runInSlot.freeFrame(runInFr);
                for (boolean writeDone = false; (writeDone == false) && !threadDead; ) {
                    try {
                        runOutSlot.writeFrame(runOutFr);
                        writeDone = true;
                        runFrameDone(runOutSlot, runOutFr);
                        runOutStream.freeFrame(runOutFr);
                    } catch (InterruptedException e) {
                    }
                    runCheckPause();
                }
            }
            runInStream.closeReader();
            runOutStream.closeWriter();
        } catch (IOException e) {
            runQuit(e);
            return;
        } catch (SlotAlreadyConnectedException e) {
            runQuit(e);
            return;
        }
        runQuit(null);
    }
