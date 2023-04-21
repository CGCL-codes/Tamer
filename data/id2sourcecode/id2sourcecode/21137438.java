    public void run() {
        runInit();
        String runFileName;
        SpectralFile runFile = null;
        int runStartFrame;
        int runFrames;
        int runFramesRead = 0;
        SpectStreamSlot runOutSlot;
        SpectStream runInStream;
        SpectStream runOutStream;
        SpectFrame runInFr;
        SpectFrame runOutFr;
        Param inLength;
        double startShift;
        double outLength;
        if ((pr.text[PR_FILENAME] == null) || (pr.text[PR_FILENAME].length() == 0)) {
            FileDialog fDlg;
            String fiName, fiDir;
            final Component ownerF = owner.getWindow().getWindow();
            final boolean makeF = !(ownerF instanceof Frame);
            final Frame f = makeF ? new Frame() : (Frame) ownerF;
            fDlg = new FileDialog(f, ((OpIcon) getIcon()).getName() + ": Select inputfile");
            fDlg.setVisible(true);
            if (makeF) f.dispose();
            fiName = fDlg.getFile();
            fiDir = fDlg.getDirectory();
            fDlg.dispose();
            if (fiDir == null) fiDir = "";
            if (fiName == null) {
                runQuit(new IOException(ERR_NOINPUT));
                return;
            }
            runFileName = fiDir + fiName;
        } else {
            runFileName = pr.text[PR_FILENAME];
        }
        try {
            runFile = new SpectralFile(runFileName, GenericFile.MODE_INPUT | (pr.bool[PR_REMOVEDC] ? SpectralFile.MODE_REMOVEDC : 0));
            runInStream = runFile.getDescr();
            inLength = new Param(SpectStream.framesToMillis(runInStream, runInStream.frames), Param.ABS_MS);
            startShift = Param.transform(pr.para[PR_STARTSHIFT], Param.ABS_MS, inLength, runInStream).val;
            outLength = Param.transform(pr.para[PR_LENGTH], Param.ABS_MS, inLength, runInStream).val;
            if (pr.bool[PR_ADJUSTSTART]) {
                runStartFrame = (int) SpectStream.millisToFrames(runInStream, startShift);
                if (runStartFrame >= runInStream.frames) {
                    runStartFrame = runInStream.frames;
                }
            } else {
                runStartFrame = 0;
            }
            runFile.seekFrame(runStartFrame);
            if (pr.bool[PR_ADJUSTLENGTH]) {
                runFrames = (int) SpectStream.millisToFrames(runInStream, outLength);
                if (runStartFrame + runFrames > runInStream.frames) {
                    runFrames = runInStream.frames - runStartFrame;
                }
            } else {
                runFrames = runInStream.frames - runStartFrame;
            }
            runInStream.setEstimatedLength(runFrames);
            runOutStream = new SpectStream(runInStream);
            if (pr.intg[PR_CHANNELS] != PR_CHANNELS_UNTOUCHED) {
                runOutStream.setChannels(1);
            }
            runOutSlot = ((SpectStreamSlot) slots.elementAt(SLOT_OUTPUT));
            runOutSlot.initWriter(runOutStream);
            runSlotsReady();
            while ((runFramesRead < runFrames) && !threadDead) {
                runInFr = runFile.readFrame();
                runFramesRead++;
                runOutFr = new SpectFrame(runInFr, pr.intg[PR_CHANNELS]);
                runFile.freeFrame(runInFr);
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
            runFile.close();
            runOutStream.closeWriter();
        } catch (IOException e) {
            if (runFile != null) {
                runFile.cleanUp();
            }
            runQuit(e);
            return;
        } catch (SlotAlreadyConnectedException e) {
            if (runFile != null) {
                runFile.cleanUp();
            }
            runQuit(e);
            return;
        }
        runQuit(null);
    }
