    public Object executeUpdate(Object parsedObject, int queryTimeOut) throws DException {
        if (parsedObject instanceof SQLdatastatement) {
            if (((String) getUserSession().getTransactionAccessMode()).equalsIgnoreCase("Read Only")) throw new DException("DSE1184", (Object[]) null);
            _StatementExecutionContext sec = getStatementExecutionContext();
            _Executer executer = (_Executer) ((SQLdatastatement) parsedObject).run(sec);
            Object returnedObject = executer.execute((_VariableValues) null);
            if (returnedObject == null) return new Integer(Integer.MIN_VALUE);
            return returnedObject;
        } else if (parsedObject instanceof SQLschemastatement) {
            commit();
            _ServerSession ss = getSystemServerSession();
            if (((String) getUserSession().getTransactionAccessMode()).equalsIgnoreCase("Read Only")) throw new DException("DSE1184", (Object[]) null);
            Object returnedObject = null;
            _DataDictionary dd = getDataDictionary();
            dd.lockDDL();
            try {
                try {
                    returnedObject = ((SQLschemastatement) parsedObject).run(this);
                } catch (DException ex) {
                    dd.restoreGeneratedKeys();
                    createIndexesList.clear();
                    ss.rollback();
                    throw ex;
                }
                if (!(parsedObject instanceof dropdatabasestatement)) {
                    ((ServerSession) ss).createIndexesList = createIndexesList;
                    ss.commit();
                    createIndexesList.clear();
                }
            } finally {
                dd.releaseDDL();
            }
            if (returnedObject == null) return new Integer(Integer.MIN_VALUE);
            return returnedObject;
        }
        throw new DException("DSE533", null);
    }
