        public synchronized void commit() throws DataServiceException {
            long commitStartTime = System.currentTimeMillis();
            mConnection = openConnection();
            if (System.currentTimeMillis() - commitStartTime > 50) Log.printWarning(this, "Long connection open time: " + (System.currentTimeMillis() - commitStartTime) + " ms");
            try {
                try {
                    if (mConnectionInfo.containsKey("catalog")) mConnection.setCatalog((String) mConnectionInfo.get("catalog"));
                } catch (SQLException e) {
                }
                try {
                    mConnection.setAutoCommit(false);
                } catch (Exception e) {
                    Log.printWarning(this, "Transaction used without database support!");
                }
                Enumeration dataOperations = this.getOperations();
                mNumberOfOperations.set(new Integer(this.getNbrOfOperations()));
                mTransaction.set(this);
                mNbrOfOperations = this.getNbrOfOperations();
                if (mNbrOfOperations > 1) this.updateProgress(this.getNbrOfOperations() * 100, 0);
                mCurrentOperation = 1;
                while (dataOperations.hasMoreElements()) {
                    DataOperation operation = (DataOperation) dataOperations.nextElement();
                    switch(operation.getOperationType()) {
                        case DataOperation.LOAD:
                            executeLoad(mConnection, operation.getEntity(), operation.getQualifier());
                            break;
                        case DataOperation.QUERY:
                            executeQuery(mConnection, operation.getDataQuery(), operation.getEntitySelection());
                            break;
                        case DataOperation.STORE:
                            if (operation.getEntity().isPersistent()) executeUpdate(mConnection, operation.getEntity(), operation.getQualifier()); else executeInsert(mConnection, operation.getEntity());
                            break;
                        case DataOperation.DELETE:
                            executeDelete(mConnection, operation.getEntity());
                            break;
                        case DataOperation.REFRESH:
                            executeLoad(mConnection, operation.getEntity(), operation.getQualifier());
                            break;
                        case DataOperation.CREATE_TABLE:
                            executeCreateTable(mConnection, operation.getEntityDescriptor());
                            break;
                        case DataOperation.CREATE_RELATION:
                            executeCreateRelation(mConnection, operation.getEntityRelation());
                            break;
                    }
                    if (mNbrOfOperations > 1) this.updateProgress(mCurrentOperation * 100);
                    mCurrentOperation++;
                }
                mConnection.commit();
                Log.print(this, "Commit time: " + (System.currentTimeMillis() - commitStartTime) + " ms");
                dataOperations = this.getOperations();
                while (dataOperations.hasMoreElements()) {
                    DataOperation operation = (DataOperation) dataOperations.nextElement();
                    switch(operation.getOperationType()) {
                        case DataOperation.DELETE:
                            operation.getEntity().clearStatusFlag(IEntity.DIRTY | IEntity.EMPTY | IEntity.PERSISTENT);
                            break;
                        case DataOperation.STORE:
                            operation.getEntity().setStatusFlag(IEntity.PERSISTENT);
                            operation.getEntity().clearStatusFlag(IEntity.DIRTY | IEntity.EMPTY);
                            break;
                        case DataOperation.REFRESH:
                        case DataOperation.LOAD:
                        case DataOperation.QUERY:
                            break;
                    }
                }
            } catch (Exception e) {
                String rollbackMsg;
                try {
                    mConnection.rollback();
                    rollbackMsg = "rollback succesfull";
                } catch (SQLException eSQL) {
                    rollbackMsg = "rollback failed";
                }
                if (!mIsAborted) {
                    if (e instanceof DataServiceException) throw new DataServiceException("Transaction failed, " + rollbackMsg, e, ((DataServiceException) e).getDescription()); else throw new DataServiceException("Transaction failed, " + rollbackMsg + ": " + e.getClass().getName() + " - " + e.getMessage(), e);
                }
            } finally {
                synchronized (mConnection) {
                    closeConnection(mConnection);
                    mConnection = null;
                    mIsAborted = false;
                }
            }
        }
