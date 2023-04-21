    private void process() throws IOException, SQLException {
        int operation = transfer.readInt();
        switch(operation) {
            case SessionRemote.SESSION_PREPARE_READ_PARAMS:
            case SessionRemote.SESSION_PREPARE:
                {
                    int id = transfer.readInt();
                    String sql = transfer.readString();
                    int old = session.getModificationId();
                    Command command = session.prepareLocal(sql);
                    boolean readonly = command.isReadOnly();
                    cache.addObject(id, command);
                    boolean isQuery = command.isQuery();
                    ObjectArray<? extends ParameterInterface> params = command.getParameters();
                    transfer.writeInt(getState(old)).writeBoolean(isQuery).writeBoolean(readonly).writeInt(params.size());
                    if (operation == SessionRemote.SESSION_PREPARE_READ_PARAMS) {
                        for (ParameterInterface p : params) {
                            ParameterRemote.writeMetaData(transfer, p);
                        }
                    }
                    transfer.flush();
                    break;
                }
            case SessionRemote.SESSION_CLOSE:
                {
                    closeSession();
                    transfer.writeInt(SessionRemote.STATUS_OK).flush();
                    close();
                    break;
                }
            case SessionRemote.COMMAND_COMMIT:
                {
                    if (commit == null) {
                        commit = session.prepareLocal("COMMIT");
                    }
                    int old = session.getModificationId();
                    commit.executeUpdate();
                    transfer.writeInt(getState(old)).flush();
                    break;
                }
            case SessionRemote.COMMAND_GET_META_DATA:
                {
                    int id = transfer.readInt();
                    int objectId = transfer.readInt();
                    Command command = (Command) cache.getObject(id, false);
                    LocalResult result = command.getMetaDataLocal();
                    cache.addObject(objectId, result);
                    int columnCount = result.getVisibleColumnCount();
                    transfer.writeInt(SessionRemote.STATUS_OK).writeInt(columnCount).writeInt(0);
                    for (int i = 0; i < columnCount; i++) {
                        ResultColumn.writeColumn(transfer, result, i);
                    }
                    transfer.flush();
                    break;
                }
            case SessionRemote.COMMAND_EXECUTE_QUERY:
                {
                    int id = transfer.readInt();
                    int objectId = transfer.readInt();
                    int maxRows = transfer.readInt();
                    int fetchSize = transfer.readInt();
                    Command command = (Command) cache.getObject(id, false);
                    setParameters(command);
                    int old = session.getModificationId();
                    LocalResult result = command.executeQueryLocal(maxRows);
                    cache.addObject(objectId, result);
                    int columnCount = result.getVisibleColumnCount();
                    int state = getState(old);
                    transfer.writeInt(state).writeInt(columnCount);
                    int rowCount = result.getRowCount();
                    transfer.writeInt(rowCount);
                    for (int i = 0; i < columnCount; i++) {
                        ResultColumn.writeColumn(transfer, result, i);
                    }
                    int fetch = Math.min(rowCount, fetchSize);
                    for (int i = 0; i < fetch; i++) {
                        sendRow(result);
                    }
                    transfer.flush();
                    break;
                }
            case SessionRemote.COMMAND_EXECUTE_UPDATE:
                {
                    int id = transfer.readInt();
                    Command command = (Command) cache.getObject(id, false);
                    setParameters(command);
                    int old = session.getModificationId();
                    int updateCount = command.executeUpdate();
                    int status;
                    if (session.isClosed()) {
                        status = SessionRemote.STATUS_CLOSED;
                    } else {
                        status = getState(old);
                    }
                    transfer.writeInt(status).writeInt(updateCount).writeBoolean(session.getAutoCommit());
                    transfer.flush();
                    break;
                }
            case SessionRemote.COMMAND_CLOSE:
                {
                    int id = transfer.readInt();
                    Command command = (Command) cache.getObject(id, true);
                    if (command != null) {
                        command.close();
                        cache.freeObject(id);
                    }
                    break;
                }
            case SessionRemote.RESULT_FETCH_ROWS:
                {
                    int id = transfer.readInt();
                    int count = transfer.readInt();
                    LocalResult result = (LocalResult) cache.getObject(id, false);
                    transfer.writeInt(SessionRemote.STATUS_OK);
                    for (int i = 0; i < count; i++) {
                        sendRow(result);
                    }
                    transfer.flush();
                    break;
                }
            case SessionRemote.RESULT_RESET:
                {
                    int id = transfer.readInt();
                    LocalResult result = (LocalResult) cache.getObject(id, false);
                    result.reset();
                    break;
                }
            case SessionRemote.RESULT_CLOSE:
                {
                    int id = transfer.readInt();
                    LocalResult result = (LocalResult) cache.getObject(id, true);
                    if (result != null) {
                        result.close();
                        cache.freeObject(id);
                    }
                    break;
                }
            case SessionRemote.CHANGE_ID:
                {
                    int oldId = transfer.readInt();
                    int newId = transfer.readInt();
                    Object obj = cache.getObject(oldId, false);
                    cache.freeObject(oldId);
                    cache.addObject(newId, obj);
                    break;
                }
            case SessionRemote.SESSION_SET_ID:
                {
                    sessionId = transfer.readString();
                    transfer.writeInt(SessionRemote.STATUS_OK).flush();
                    break;
                }
            default:
                trace("Unknown operation: " + operation);
                closeSession();
                close();
        }
    }
