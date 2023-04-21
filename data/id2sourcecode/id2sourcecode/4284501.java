    StatementCommand(int type, Object[] args, HsqlName[] readNames, HsqlName[] writeNames) {
        super(type);
        this.isTransactionStatement = true;
        this.parameters = args;
        if (readNames != null) {
            this.readTableNames = readNames;
        }
        if (writeNames != null) {
            this.writeTableNames = writeNames;
        }
        switch(type) {
            case StatementTypes.DATABASE_CHECKPOINT:
                group = StatementTypes.X_HSQLDB_DATABASE_OPERATION;
                isLogged = false;
                break;
            case StatementTypes.DATABASE_BACKUP:
            case StatementTypes.DATABASE_SCRIPT:
                group = StatementTypes.X_HSQLDB_DATABASE_OPERATION;
                isLogged = false;
                break;
            case StatementTypes.SET_DATABASE_UNIQUE_NAME:
            case StatementTypes.SET_DATABASE_FILES_WRITE_DELAY:
            case StatementTypes.SET_DATABASE_FILES_TEMP_PATH:
                this.isTransactionStatement = false;
                group = StatementTypes.X_HSQLDB_SETTING;
                break;
            case StatementTypes.SET_DATABASE_DEFAULT_INITIAL_SCHEMA:
            case StatementTypes.SET_DATABASE_DEFAULT_TABLE_TYPE:
            case StatementTypes.SET_DATABASE_FILES_CACHE_ROWS:
            case StatementTypes.SET_DATABASE_FILES_CACHE_SIZE:
            case StatementTypes.SET_DATABASE_FILES_SCALE:
            case StatementTypes.SET_DATABASE_FILES_DEFRAG:
            case StatementTypes.SET_DATABASE_FILES_EVENT_LOG:
            case StatementTypes.SET_DATABASE_FILES_LOBS_SCALE:
            case StatementTypes.SET_DATABASE_FILES_LOCK:
            case StatementTypes.SET_DATABASE_FILES_LOG:
            case StatementTypes.SET_DATABASE_FILES_LOG_SIZE:
            case StatementTypes.SET_DATABASE_FILES_NIO:
            case StatementTypes.SET_DATABASE_FILES_SCRIPT_FORMAT:
            case StatementTypes.SET_DATABASE_AUTHENTICATION:
            case StatementTypes.SET_DATABASE_PASSWORD_CHECK:
            case StatementTypes.SET_DATABASE_PROPERTY:
            case StatementTypes.SET_DATABASE_RESULT_MEMORY_ROWS:
            case StatementTypes.SET_DATABASE_SQL_REFERENTIAL_INTEGRITY:
            case StatementTypes.SET_DATABASE_SQL_STRICT:
            case StatementTypes.SET_DATABASE_TRANSACTION_CONTROL:
            case StatementTypes.SET_DATABASE_DEFAULT_ISOLATION_LEVEL:
            case StatementTypes.SET_DATABASE_GC:
            case StatementTypes.SET_DATABASE_SQL_COLLATION:
            case StatementTypes.SET_DATABASE_FILES_BACKUP_INCREMENT:
            case StatementTypes.SET_DATABASE_TEXT_SOURCE:
                group = StatementTypes.X_HSQLDB_SETTING;
                this.isTransactionStatement = true;
                break;
            case StatementTypes.SET_TABLE_CLUSTERED:
                group = StatementTypes.X_HSQLDB_SCHEMA_MANIPULATION;
                break;
            case StatementTypes.SET_TABLE_SOURCE_HEADER:
                isLogged = false;
            case StatementTypes.SET_TABLE_SOURCE:
                metaDataImpact = Statement.META_RESET_VIEWS;
                group = StatementTypes.X_HSQLDB_SCHEMA_MANIPULATION;
                this.isTransactionStatement = true;
                break;
            case StatementTypes.SET_TABLE_READONLY:
                metaDataImpact = Statement.META_RESET_VIEWS;
                group = StatementTypes.X_HSQLDB_SCHEMA_MANIPULATION;
                this.isTransactionStatement = true;
                break;
            case StatementTypes.DATABASE_SHUTDOWN:
                isLogged = false;
                group = StatementTypes.X_HSQLDB_DATABASE_OPERATION;
                this.isTransactionStatement = false;
                break;
            case StatementTypes.SET_TABLE_TYPE:
                group = StatementTypes.X_HSQLDB_SCHEMA_MANIPULATION;
                this.isTransactionStatement = true;
                break;
            case StatementTypes.SET_TABLE_INDEX:
                group = StatementTypes.X_HSQLDB_SETTING;
                this.isTransactionStatement = false;
                isLogged = false;
                break;
            case StatementTypes.SET_USER_LOCAL:
            case StatementTypes.SET_USER_INITIAL_SCHEMA:
            case StatementTypes.SET_USER_PASSWORD:
                group = StatementTypes.X_HSQLDB_SETTING;
                this.isTransactionStatement = false;
                break;
            case StatementTypes.ALTER_SESSION:
                group = StatementTypes.X_HSQLDB_SESSION;
                this.isTransactionStatement = false;
                break;
            default:
                throw Error.runtimeError(ErrorCode.U_S0500, "StatementCommand");
        }
    }
