    StatementSession(int type, HsqlName[] readNames, HsqlName[] writeNames) {
        super(type);
        this.isTransactionStatement = true;
        this.readTableNames = readNames;
        writeTableNames = writeNames;
        switch(type) {
            case StatementTypes.TRANSACTION_LOCK_TABLE:
                group = StatementTypes.X_HSQLDB_TRANSACTION;
                break;
            default:
                throw Error.runtimeError(ErrorCode.U_S0500, "StatementSession");
        }
    }
