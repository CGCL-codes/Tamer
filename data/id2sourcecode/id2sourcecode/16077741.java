    protected final void analyzeLogPart(final int startAddr, final String sData) {
        int dataLength;
        dataLength = Conv.hexStringToBytes(sData, context.readDataBuffer) / 2;
        switch(context.getLogState()) {
            case C_LOG_ACTIVE:
            case C_LOG_RECOVER:
                if (context.getLogNextReadAddr() == startAddr) {
                    context.setLogState(MTKLogDownloadHandler.C_LOG_ACTIVE);
                    int j = 0;
                    if ((dataLength != 0x800) && (dataLength != context.logRequestStep) && ((context.getLogNextReadAddr() + dataLength) != context.logNextReqAddr) && (dataLength != ((context.getLogNextReadAddr() + 0x10000) & ~0xFFFF) - context.getLogNextReadAddr())) {
                        if (Generic.isDebug()) {
                            Generic.debug("Unexpected datalength: " + JavaLibBridge.unsigned2hex(dataLength, 8));
                        }
                        context.setLogState(MTKLogDownloadHandler.C_LOG_RECOVER);
                    } else {
                        for (int i = dataLength; i > 0; i -= MTKLogDownloadHandler.C_MAX_FILEBLOCK_WRITE) {
                            int l = i;
                            if (l > MTKLogDownloadHandler.C_MAX_FILEBLOCK_WRITE) {
                                l = MTKLogDownloadHandler.C_MAX_FILEBLOCK_WRITE;
                            }
                            try {
                                if ((context.logFile.writeBytes(context.readDataBuffer, j, l)) != l) {
                                    cancelGetLog();
                                }
                            } catch (final Exception e) {
                                Generic.debug("analyzeLogPart", e);
                                cancelGetLog();
                            }
                            j += l;
                        }
                        context.setLogNextReadAddr(context.getLogNextReadAddr() + dataLength);
                        if (context.getFullLogBlocks && (((startAddr - 1 + dataLength) & 0xFFFF0000) >= startAddr)) {
                            final int blockStart = 0xFFFF & (0x10000 - (startAddr & 0xFFFF));
                            if (!(((context.readDataBuffer[blockStart] & 0xFF) == 0xFF) && ((context.readDataBuffer[blockStart + 1] & 0xFF) == 0xFF))) {
                                int minEndAddr;
                                minEndAddr = (startAddr & 0xFFFF0000) + 0x20000 - 1;
                                if (minEndAddr > context.mtkM.getLogMemSize() - 1) {
                                    minEndAddr = context.mtkM.getLogMemSize() - 1;
                                }
                                if (minEndAddr > context.getLogDownloadEndAddr()) {
                                    context.setLogDownloadEndAddr(minEndAddr);
                                }
                            }
                        }
                    }
                    if (context.getLogNextReadAddr() > context.getLogDownloadEndAddr()) {
                        context.mtkM.postEvent(GpsEvent.LOG_DOWNLOAD_SUCCESS);
                        endGetLog();
                    } else {
                        getNextLogPart();
                    }
                } else {
                    Generic.debug("Expected:" + JavaLibBridge.unsigned2hex(context.getLogNextReadAddr(), 8) + " Got:" + JavaLibBridge.unsigned2hex(startAddr, 8) + " (" + JavaLibBridge.unsigned2hex(dataLength, 8) + ")", null);
                    recoverFromLogError();
                }
                break;
            case C_LOG_CHECK:
                context.setLogState(MTKLogDownloadHandler.C_LOG_NOLOGGING);
                if ((startAddr == MTKLogDownloadHandler.C_BLOCKVERIF_START) && (dataLength == MTKLogDownloadHandler.C_BLOCKVERIF_SIZE)) {
                    boolean success;
                    success = true;
                    for (int i = dataLength - 1; i >= 0; i--) {
                        if (context.readDataBuffer[i] != context.expectedResult[i]) {
                            success = false;
                            break;
                        }
                    }
                    if (success) {
                        reOpenLogWrite(context.logPath);
                        try {
                            context.logFile.setPos(context.getLogNextReadAddr());
                        } catch (final Exception e) {
                            Generic.debug("C_LOG_CHECK", e);
                        }
                        if (Generic.isDebug()) {
                            Generic.debug("Starting incremental download from " + JavaLibBridge.unsigned2hex(context.getLogNextReadAddr(), 8) + " to " + JavaLibBridge.unsigned2hex(context.getLogDownloadEndAddr(), 8));
                        }
                        context.setLogState(MTKLogDownloadHandler.C_LOG_ACTIVE);
                        getNextLogPart();
                    } else {
                        context.setLogState(MTKLogDownloadHandler.C_LOG_DATA_NOT_SAME_WAITING_FOR_REPLY);
                        if (Generic.isDebug()) {
                            Generic.debug("Different data - requesting overwrite confirmation");
                        }
                        context.mtkM.postEvent(GpsEvent.DOWNLOAD_DATA_NOT_SAME_NEEDS_REPLY);
                    }
                } else {
                    if (Generic.isDebug()) {
                        Generic.debug("Expected:" + JavaLibBridge.unsigned2hex(MTKLogDownloadHandler.C_BLOCKVERIF_START, 8) + " Got:" + JavaLibBridge.unsigned2hex(startAddr, 8) + " (" + JavaLibBridge.unsigned2hex(dataLength, 8) + ")", null);
                    }
                    context.setLogState(MTKLogDownloadHandler.C_LOG_CHECK);
                }
                break;
            default:
                break;
        }
        if (context.getLogState() == C_LOG_NOLOGGING) {
            context.mtkM.getHandler().setLogOrEraseOngoing(false);
        }
        context.mtkM.postEvent(GpsEvent.DOWNLOAD_STATE_CHANGE);
    }
