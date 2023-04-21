    public Result invoke(Session session, Object[] data, Object[] aggregateData, boolean push) {
        Result result;
        if (push) {
            session.sessionContext.push();
        }
        if (isPSM()) {
            try {
                session.sessionContext.routineArguments = data;
                session.sessionContext.routineVariables = ValuePool.emptyObjectArray;
                if (variableCount > 0) {
                    session.sessionContext.routineVariables = new Object[variableCount];
                }
                result = statement.execute(session);
                if (aggregateData != null) {
                    for (int i = 0; i < aggregateData.length; i++) {
                        aggregateData[i] = data[i + 1];
                    }
                }
            } catch (Throwable e) {
                result = Result.newErrorResult(e);
            }
        } else {
            if (isAggregate) {
                data = convertArgsToJava(session, data);
            }
            result = invokeJavaMethod(session, data);
            if (isAggregate) {
                Object[] callResult = new Object[data.length];
                convertArgsToSQL(session, callResult, data);
                for (int i = 0; i < aggregateData.length; i++) {
                    aggregateData[i] = callResult[i + 1];
                }
            }
        }
        if (push) {
            session.sessionContext.pop();
        }
        return result;
    }
