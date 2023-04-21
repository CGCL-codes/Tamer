    public void extract(Node targetNode, IOutgoingTransport targetTransport, List<OutgoingBatch> activeBatches) {
        Node identity = nodeService.findIdentity();
        long batchesSelectedAtMs = System.currentTimeMillis();
        boolean streamToFileEnabled = parameterService.is(ParameterConstants.STREAM_TO_FILE_ENABLED);
        TransformWriter transformExtractWriter = null;
        if (streamToFileEnabled) {
            transformExtractWriter = createTransformDataWriter(identity, targetNode, new StagingDataWriter(nodeService.findIdentityNodeId(), Constants.STAGING_CATEGORY_OUTGOING, stagingManager));
        } else {
            transformExtractWriter = createTransformDataWriter(identity, targetNode, new ProtocolDataWriter(nodeService.findIdentityNodeId(), targetTransport.open()));
        }
        final long maxBytesToSync = parameterService.getLong(ParameterConstants.TRANSPORT_MAX_BYTES_TO_SYNC);
        long bytesSentCount = 0;
        int batchesSentCount = 0;
        OutgoingBatch currentBatch = null;
        try {
            IDataWriter dataWriter = null;
            for (int i = 0; i < activeBatches.size(); i++) {
                currentBatch = activeBatches.get(i);
                if (System.currentTimeMillis() - batchesSelectedAtMs > MS_PASSED_BEFORE_BATCH_REQUERIED) {
                    currentBatch = outgoingBatchService.findOutgoingBatch(currentBatch.getBatchId());
                }
                if (currentBatch.getStatus() != Status.OK) {
                    long extractTimeInMs = 0l;
                    long byteCount = 0l;
                    IStagedResource previouslyExtracted = stagingManager.find(Constants.STAGING_CATEGORY_OUTGOING, currentBatch.getNodeId(), currentBatch.getBatchId());
                    if (previouslyExtracted != null && previouslyExtracted.exists()) {
                        log.info("We have already extracted batch {}.  Using the existing extraction: {}", currentBatch.getBatchId(), previouslyExtracted);
                    } else {
                        currentBatch.setStatus(OutgoingBatch.Status.QY);
                        currentBatch.setExtractCount(currentBatch.getExtractCount() + 1);
                        outgoingBatchService.updateOutgoingBatch(currentBatch);
                        long ts = System.currentTimeMillis();
                        IDataReader dataReader = new ExtractDataReader(symmetricDialect.getPlatform(), new SelectFromSymDataSource(currentBatch, targetNode));
                        new DataProcessor(dataReader, transformExtractWriter).process();
                        extractTimeInMs = System.currentTimeMillis() - ts;
                        Statistics stats = transformExtractWriter.getTargetWriter().getStatistics().values().iterator().next();
                        byteCount = stats.get(DataWriterStatisticConstants.BYTECOUNT);
                    }
                    if (System.currentTimeMillis() - currentBatch.getLastUpdatedTime().getTime() > MS_PASSED_BEFORE_BATCH_REQUERIED) {
                        currentBatch = outgoingBatchService.findOutgoingBatch(currentBatch.getBatchId());
                    }
                    if (extractTimeInMs > 0) {
                        currentBatch.setExtractMillis(extractTimeInMs);
                    }
                    if (byteCount > 0) {
                        currentBatch.setByteCount(byteCount);
                    }
                    if (currentBatch.getStatus() != Status.OK) {
                        currentBatch.setStatus(OutgoingBatch.Status.SE);
                        currentBatch.setSentCount(currentBatch.getSentCount() + 1);
                        outgoingBatchService.updateOutgoingBatch(currentBatch);
                        IStagedResource extractedBatch = stagingManager.find(Constants.STAGING_CATEGORY_OUTGOING, currentBatch.getNodeId(), currentBatch.getBatchId());
                        if (extractedBatch != null) {
                            IDataReader dataReader = new ProtocolDataReader(extractedBatch);
                            if (dataWriter == null) {
                                dataWriter = new ProtocolDataWriter(nodeService.findIdentityNodeId(), targetTransport.open());
                            }
                            new DataProcessor(dataReader, dataWriter).process();
                        }
                        if (System.currentTimeMillis() - currentBatch.getLastUpdatedTime().getTime() > MS_PASSED_BEFORE_BATCH_REQUERIED) {
                            currentBatch = outgoingBatchService.findOutgoingBatch(currentBatch.getBatchId());
                        }
                        if (currentBatch.getStatus() != Status.OK) {
                            currentBatch.setStatus(OutgoingBatch.Status.LD);
                            currentBatch.setLoadCount(currentBatch.getLoadCount() + 1);
                            outgoingBatchService.updateOutgoingBatch(currentBatch);
                            bytesSentCount += currentBatch.getByteCount();
                            batchesSentCount++;
                            if (bytesSentCount >= maxBytesToSync) {
                                log.info("Reached byte threshold after {} batches at {} bytes.  Data will continue to be synchronized on the next pull", batchesSentCount, bytesSentCount);
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            SQLException se = unwrapSqlException(e);
            if (currentBatch != null) {
                statisticManager.incrementDataExtractedErrors(currentBatch.getChannelId(), 1);
                if (se != null) {
                    currentBatch.setSqlState(se.getSQLState());
                    currentBatch.setSqlCode(se.getErrorCode());
                    currentBatch.setSqlMessage(se.getMessage());
                } else {
                    currentBatch.setSqlMessage(e.getMessage());
                }
                currentBatch.revertStatsOnError();
                currentBatch.setStatus(OutgoingBatch.Status.ER);
                currentBatch.setErrorFlag(true);
                outgoingBatchService.updateOutgoingBatch(currentBatch);
            } else {
                log.error("Could not log the outgoing batch status because the batch was null.", e);
            }
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else {
                throw new RuntimeException(e);
            }
        }
    }
