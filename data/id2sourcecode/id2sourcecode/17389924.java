    public void run() {
        runInit();
        final SpectStreamSlot runInSlot;
        final SpectStreamSlot runOutSlot;
        SpectStream runInStream = null;
        final SpectStream runOutStream;
        SpectFrame runInFr = null;
        SpectFrame runOutFr = null;
        float gain;
        float[] convBuf1;
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
            runOutSlot = (SpectStreamSlot) slots.elementAt(SLOT_OUTPUT);
            runOutStream = new SpectStream(runInStream);
            runOutSlot.initWriter(runOutStream);
            final Modulator envMod = new Modulator(new Param(0.0, Param.ABS_AMP), new Param(1.0, Param.ABS_AMP), pr.envl[PR_ENV], runInStream);
            runSlotsReady();
            mainLoop: while (!threadDead) {
                gain = (float) envMod.calc().val;
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
                Util.copy(runInFr.data, 0, runOutFr.data, 0, runInStream.bands << 1);
                for (int ch = 0; ch < runInStream.chanNum; ch++) {
                    convBuf1 = runOutFr.data[ch];
                    for (int i = 0, j = 0; j < runInStream.bands; i += 2, j++) {
                        convBuf1[i] *= gain;
                    }
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
