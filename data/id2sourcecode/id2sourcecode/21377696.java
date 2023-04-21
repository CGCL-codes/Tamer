        public void batchInError(DataContext context, Exception ex) {
            try {
                Batch batch = context.getBatch();
                if (context.getWriter() != null) {
                    this.currentBatch.setValues(context.getReader().getStatistics().get(batch), context.getWriter().getStatistics().get(batch), false);
                } else {
                    log.error("An error caused a batch to fail without attempting to load data", ex);
                    ex.printStackTrace();
                }
                enableSyncTriggers(context);
                statisticManager.incrementDataLoadedErrors(this.currentBatch.getChannelId(), 1);
                if (ex instanceof IOException || ex instanceof TransportException) {
                    log.warn("Failed to load batch {} because: {}", this.currentBatch.getNodeBatchId(), ex.getMessage());
                    this.currentBatch.setSqlMessage(ex.getMessage());
                } else {
                    log.error("Failed to load batch {} because: {}", new Object[] { this.currentBatch.getNodeBatchId(), ex.getMessage() });
                    log.error(ex.getMessage(), ex);
                    SQLException se = unwrapSqlException(ex);
                    if (se != null) {
                        this.currentBatch.setSqlState(se.getSQLState());
                        this.currentBatch.setSqlCode(se.getErrorCode());
                        this.currentBatch.setSqlMessage(se.getMessage());
                    } else {
                        this.currentBatch.setSqlMessage(ex.getMessage());
                    }
                }
                if (this.currentBatch.getStatus() != Status.OK) {
                    this.currentBatch.setStatus(IncomingBatch.Status.ER);
                    if (context.getTable() != null) {
                        try {
                            IncomingError error = new IncomingError();
                            error.setBatchId(this.currentBatch.getBatchId());
                            error.setNodeId(this.currentBatch.getNodeId());
                            error.setColumnNames(Table.getCommaDeliminatedColumns(context.getTable().getColumns()));
                            error.setPrimaryKeyColumnNames(Table.getCommaDeliminatedColumns(context.getTable().getPrimaryKeyColumns()));
                            error.setCsvData(context.getData());
                            error.setEventType(context.getData().getDataEventType());
                            error.setFailedLineNumber(this.currentBatch.getFailedLineNumber());
                            error.setFailedRowNumber(this.currentBatch.getFailedRowNumber());
                            error.setTargetCatalogName(context.getTable().getCatalog());
                            error.setTargetSchemaName(context.getTable().getSchema());
                            error.setTargetTableName(context.getTable().getName());
                            insertIncomingError(error);
                        } catch (UniqueKeyException e) {
                        }
                    }
                }
                incomingBatchService.updateIncomingBatch(this.currentBatch);
            } catch (Exception e) {
                log.error("Failed to record status of batch {} because {}", this.currentBatch != null ? this.currentBatch.getNodeBatchId() : context.getBatch().getNodeBatchId(), e.getMessage());
                e.printStackTrace();
            }
        }
