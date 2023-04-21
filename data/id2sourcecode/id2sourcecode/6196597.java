        public void commit() throws DataServiceException {
            long commitStartTime = System.currentTimeMillis();
            mConnection = openConnection();
            if (System.currentTimeMillis() - commitStartTime > 50) Log.printWarning(this, "Long connection open time: " + (System.currentTimeMillis() - commitStartTime) + " ms");
            try {
                try {
                    if (mConnectionInfo.containsKey("catalog")) mConnection.setCatalog((String) mConnectionInfo.get("catalog"));
                } catch (SQLException e) {
                }
                mConnection.setAutoCommit(false);
                Enumeration dataOperations = this.getOperations();
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
                    }
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
                if (e instanceof DataServiceException) throw new DataServiceException("Transaction failed, " + rollbackMsg, e, ((DataServiceException) e).getDescription()); else throw new DataServiceException("Transaction failed, " + rollbackMsg + ": " + e.getClass().getName() + " - " + e.getMessage(), e);
            } finally {
                closeConnection(mConnection);
                mConnection = null;
            }
        }
