    @Override
    public void process() {
        Buffer inputBuffer, outputBuffer;
        do {
            if (readPendingFlag) inputBuffer = storedInputBuffer; else {
                Format incomingFormat;
                inputBuffer = ic.getValidBuffer();
                incomingFormat = inputBuffer.getFormat();
                if (incomingFormat == null) {
                    incomingFormat = ic.getFormat();
                    inputBuffer.setFormat(incomingFormat);
                }
                if (incomingFormat != ic.getFormat() && incomingFormat != null && !incomingFormat.equals(ic.getFormat()) && !inputBuffer.isDiscard()) {
                    if (writePendingFlag) {
                        storedOutputBuffer.setDiscard(true);
                        oc.writeReport();
                        writePendingFlag = false;
                    }
                    if (!reinitCodec(inputBuffer.getFormat())) {
                        inputBuffer.setDiscard(true);
                        ic.readReport();
                        failed = true;
                        if (moduleListener != null) moduleListener.formatChangedFailure(this, ic.getFormat(), inputBuffer.getFormat());
                        return;
                    }
                    Format oldFormat = ic.getFormat();
                    ic.setFormat(inputBuffer.getFormat());
                    if (moduleListener != null) moduleListener.formatChanged(this, oldFormat, inputBuffer.getFormat());
                }
                if ((inputBuffer.getFlags() & Buffer.FLAG_SYSTEM_MARKER) != 0) {
                    markerSet = true;
                }
            }
            if (writePendingFlag) outputBuffer = storedOutputBuffer; else {
                outputBuffer = oc.getEmptyBuffer();
                if (outputBuffer != null) {
                    outputBuffer.setLength(0);
                    outputBuffer.setOffset(0);
                    lastHdr = outputBuffer.getHeader();
                }
            }
            outputBuffer.setTimeStamp(inputBuffer.getTimeStamp());
            outputBuffer.setDuration(inputBuffer.getDuration());
            outputBuffer.setSequenceNumber(inputBuffer.getSequenceNumber());
            outputBuffer.setFlags(inputBuffer.getFlags());
            outputBuffer.setHeader(inputBuffer.getHeader());
            if (resetted) {
                if ((inputBuffer.getFlags() & Buffer.FLAG_FLUSH) != 0) {
                    codec.reset();
                    resetted = false;
                }
                readPendingFlag = writePendingFlag = false;
                ic.readReport();
                oc.writeReport();
                return;
            }
            if (failed || inputBuffer.isDiscard()) {
                if (markerSet) {
                    outputBuffer.setFlags(outputBuffer.getFlags() & ~Buffer.FLAG_SYSTEM_MARKER);
                    markerSet = false;
                }
                curFramesBehind = 0;
                ic.readReport();
                if (!writePendingFlag) oc.writeReport();
                return;
            }
            if (frameControl != null && curFramesBehind != prevFramesBehind && (inputBuffer.getFlags() & Buffer.FLAG_NO_DROP) == 0) {
                frameControl.setFramesBehind(curFramesBehind);
                prevFramesBehind = curFramesBehind;
            }
            int rc = 0;
            try {
                rc = codec.process(inputBuffer, outputBuffer);
            } catch (Throwable e) {
                Log.dumpStack(e);
                if (moduleListener != null) moduleListener.internalErrorOccurred(this);
            }
            if (PlaybackEngine.TRACE_ON && !verifyBuffer(outputBuffer)) {
                System.err.println("verify buffer failed: " + codec);
                Thread.dumpStack();
                if (moduleListener != null) moduleListener.internalErrorOccurred(this);
            }
            if ((rc & PlugIn.PLUGIN_TERMINATED) != 0) {
                failed = true;
                if (moduleListener != null) moduleListener.pluginTerminated(this);
                readPendingFlag = writePendingFlag = false;
                ic.readReport();
                oc.writeReport();
                return;
            }
            if (curFramesBehind > 0f && outputBuffer.isDiscard()) {
                curFramesBehind -= 1.0f;
                if (curFramesBehind < 0) curFramesBehind = 0f;
                rc = rc & ~PlugIn.OUTPUT_BUFFER_NOT_FILLED;
            }
            if ((rc & PlugIn.BUFFER_PROCESSED_FAILED) != 0) {
                outputBuffer.setDiscard(true);
                if (markerSet) {
                    outputBuffer.setFlags(outputBuffer.getFlags() & ~Buffer.FLAG_SYSTEM_MARKER);
                    markerSet = false;
                }
                ic.readReport();
                oc.writeReport();
                readPendingFlag = writePendingFlag = false;
                return;
            }
            if (outputBuffer.isEOM() && ((rc & PlugIn.INPUT_BUFFER_NOT_CONSUMED) != 0 || (rc & PlugIn.OUTPUT_BUFFER_NOT_FILLED) != 0)) {
                outputBuffer.setEOM(false);
            }
            if ((rc & PlugIn.OUTPUT_BUFFER_NOT_FILLED) != 0) {
                writePendingFlag = true;
                storedOutputBuffer = outputBuffer;
            } else {
                if (markerSet) {
                    outputBuffer.setFlags(outputBuffer.getFlags() | Buffer.FLAG_SYSTEM_MARKER);
                    markerSet = false;
                }
                oc.writeReport();
                writePendingFlag = false;
            }
            if (((rc & PlugIn.INPUT_BUFFER_NOT_CONSUMED) != 0 || (inputBuffer.isEOM() && !outputBuffer.isEOM()))) {
                readPendingFlag = true;
                storedInputBuffer = inputBuffer;
            } else {
                inputBuffer.setHeader(lastHdr);
                ic.readReport();
                readPendingFlag = false;
            }
        } while (readPendingFlag);
    }
