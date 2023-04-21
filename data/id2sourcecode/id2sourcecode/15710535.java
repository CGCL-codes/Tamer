    private void doLoop() throws Exception {
        long sleep;
        long tfinal = System.currentTimeMillis();
        while (getStatus() == MANAGERSTATUS.RUNNING && opc.getConnectionStatus() == STATUS.CONNECTED && writer.isReady()) {
            synchronized (sleepMutex) {
                final int localSamplingRate = getSamplingRate();
                tfinal += localSamplingRate;
                final Map<String, ItemState> newItemData;
                synchronized (opcDataProviderLock) {
                    newItemData = opcDataProvider.getOPCData();
                }
                final long cTime = System.currentTimeMillis();
                final Map<String, ScriptDataResult> newCompData = scriptDataProvider.getScriptData(selection.composite, newItemData);
                final Map<String, OPCItemData> newCastedItemData = new HashMap<String, OPCItemData>(newItemData.size());
                for (Entry<String, AbstractOPCItemInfo> e : item2infoMap.entrySet()) {
                    final String id = e.getKey();
                    final ItemState state = newItemData.get(e.getKey());
                    if (state != null) {
                        final JIVariant variant = state.getValue();
                        Object casted = null;
                        if (variant != null && variant.getType() != JIVariant.VT_EMPTY && !variant.isNull()) {
                            final AbstractOPCItemInfo info = e.getValue();
                            JIVariant2JavaConverter converter = info.getCasting();
                            if (converter.getJIVariantType() != variant.getType()) {
                                info.updateCasting(variant.getType());
                            }
                            try {
                                casted = converter.convert(variant);
                            } catch (JIException ex) {
                                logger.log(Level.WARNING, "Error while converting value for ''{0}'' to a {1}: {2}", new String[] { id, info.getOutputDataType().getSimpleName(), ex.getMessage() });
                            }
                        }
                        newCastedItemData.put(id, new OPCItemData(casted, state));
                    }
                }
                final OutputData oData = new OutputData(cTime, newCastedItemData, newCompData);
                sleep = tfinal - cTime;
                if (sleep <= 0) {
                    tfinal = System.currentTimeMillis();
                    if (getStatus() != MANAGERSTATUS.RUNNING || opc.getConnectionStatus() != STATUS.CONNECTED) {
                        break;
                    }
                    logger.warning("Currently unable to read data from the OPC Server at the selected update rate!\nThe data is being read as fast as possible.\nPerhaps the update rate is too low.");
                }
                if (!writerQueue.addData(oData)) {
                    logger.log(Level.WARNING, "Currently unable to write data to the output {0} at the selected update rate!\nThe data is being read as fast as possible.\nPerhaps the update rate is too low.", writer.getOutputTypeName());
                    if (!writerQueue.addData(oData, localSamplingRate)) {
                        writerQueue.forceblyAddData(oData);
                    }
                    tfinal = System.currentTimeMillis();
                    sleep = 0;
                }
            }
            if (sleep > 0) {
                try {
                    synchronized (sleepMutex) {
                        sleepMutex.wait(sleep);
                    }
                } catch (InterruptedException ex) {
                    if (getStatus() != MANAGERSTATUS.RUNNING) {
                        return;
                    } else {
                        throw ex;
                    }
                }
            }
        }
    }
