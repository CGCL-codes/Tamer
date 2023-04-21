    StatementSchema(String sql, int type, Object[] args, HsqlName[] readName, HsqlName[] writeName) {
        super(type);
        isTransactionStatement = true;
        this.sql = sql;
        if (args != null) {
            arguments = args;
        }
        if (readName != null) {
            readTableNames = readName;
        }
        if (writeName != null) {
            writeTableNames = writeName;
        }
        switch(type) {
            case StatementTypes.RENAME_OBJECT:
                group = StatementTypes.X_SQL_SCHEMA_MANIPULATION;
                break;
            case StatementTypes.ALTER_DOMAIN:
            case StatementTypes.ALTER_ROUTINE:
            case StatementTypes.ALTER_SEQUENCE:
            case StatementTypes.ALTER_TYPE:
            case StatementTypes.ALTER_TABLE:
            case StatementTypes.ALTER_TRANSFORM:
            case StatementTypes.ALTER_VIEW:
                group = StatementTypes.X_SQL_SCHEMA_MANIPULATION;
                break;
            case StatementTypes.DROP_ASSERTION:
            case StatementTypes.DROP_CHARACTER_SET:
            case StatementTypes.DROP_COLLATION:
            case StatementTypes.DROP_TYPE:
            case StatementTypes.DROP_DOMAIN:
            case StatementTypes.DROP_ROLE:
            case StatementTypes.DROP_USER:
            case StatementTypes.DROP_ROUTINE:
            case StatementTypes.DROP_SCHEMA:
            case StatementTypes.DROP_SEQUENCE:
            case StatementTypes.DROP_TABLE:
            case StatementTypes.DROP_TRANSFORM:
            case StatementTypes.DROP_TRANSLATION:
            case StatementTypes.DROP_TRIGGER:
            case StatementTypes.DROP_CAST:
            case StatementTypes.DROP_ORDERING:
            case StatementTypes.DROP_VIEW:
            case StatementTypes.DROP_INDEX:
            case StatementTypes.DROP_CONSTRAINT:
            case StatementTypes.DROP_COLUMN:
                group = StatementTypes.X_SQL_SCHEMA_MANIPULATION;
                break;
            case StatementTypes.GRANT:
                group = StatementTypes.X_SQL_SCHEMA_MANIPULATION;
                order = 10;
                break;
            case StatementTypes.GRANT_ROLE:
                group = StatementTypes.X_SQL_SCHEMA_MANIPULATION;
                order = 10;
                break;
            case StatementTypes.REVOKE:
            case StatementTypes.REVOKE_ROLE:
                group = StatementTypes.X_SQL_SCHEMA_MANIPULATION;
                break;
            case StatementTypes.CREATE_SCHEMA:
                group = StatementTypes.X_SQL_SCHEMA_DEFINITION;
                break;
            case StatementTypes.CREATE_ROLE:
                group = StatementTypes.X_SQL_SCHEMA_DEFINITION;
                order = 1;
                break;
            case StatementTypes.CREATE_ROUTINE:
                group = StatementTypes.X_SQL_SCHEMA_DEFINITION;
                order = 7;
                break;
            case StatementTypes.CREATE_SEQUENCE:
                group = StatementTypes.X_SQL_SCHEMA_DEFINITION;
                order = 1;
                break;
            case StatementTypes.CREATE_TABLE:
                group = StatementTypes.X_SQL_SCHEMA_DEFINITION;
                order = 2;
                break;
            case StatementTypes.CREATE_TRANSFORM:
                group = StatementTypes.X_SQL_SCHEMA_DEFINITION;
                order = 1;
                break;
            case StatementTypes.CREATE_TRANSLATION:
                group = StatementTypes.X_SQL_SCHEMA_DEFINITION;
                order = 1;
                break;
            case StatementTypes.CREATE_TRIGGER:
                group = StatementTypes.X_SQL_SCHEMA_DEFINITION;
                order = 7;
                break;
            case StatementTypes.CREATE_CAST:
                group = StatementTypes.X_SQL_SCHEMA_DEFINITION;
                order = 2;
                break;
            case StatementTypes.CREATE_TYPE:
                group = StatementTypes.X_SQL_SCHEMA_DEFINITION;
                order = 1;
                break;
            case StatementTypes.CREATE_ORDERING:
                group = StatementTypes.X_SQL_SCHEMA_DEFINITION;
                order = 1;
                break;
            case StatementTypes.CREATE_VIEW:
                group = StatementTypes.X_SQL_SCHEMA_DEFINITION;
                order = 5;
                break;
            case StatementTypes.CREATE_USER:
                group = StatementTypes.X_SQL_SCHEMA_DEFINITION;
                order = 1;
                break;
            case StatementTypes.CREATE_ASSERTION:
                group = StatementTypes.X_SQL_SCHEMA_DEFINITION;
                order = 9;
                break;
            case StatementTypes.CREATE_CHARACTER_SET:
                group = StatementTypes.X_SQL_SCHEMA_DEFINITION;
                order = 1;
                break;
            case StatementTypes.CREATE_COLLATION:
                group = StatementTypes.X_SQL_SCHEMA_DEFINITION;
                order = 1;
                break;
            case StatementTypes.CREATE_DOMAIN:
                group = StatementTypes.X_SQL_SCHEMA_DEFINITION;
                order = 1;
                break;
            case StatementTypes.CREATE_ALIAS:
                group = StatementTypes.X_SQL_SCHEMA_DEFINITION;
                order = 8;
                break;
            case StatementTypes.CREATE_INDEX:
                group = StatementTypes.X_SQL_SCHEMA_MANIPULATION;
                order = 4;
                break;
            case StatementTypes.COMMENT:
                group = StatementTypes.X_SQL_SCHEMA_MANIPULATION;
                order = 11;
                break;
            case StatementTypes.CHECK:
                group = StatementTypes.X_SQL_SCHEMA_MANIPULATION;
                statementTokens = (Token[]) args[0];
                break;
            case StatementTypes.LOG_SCHEMA_STATEMENT:
                group = StatementTypes.X_SQL_SCHEMA_MANIPULATION;
                break;
            default:
                throw Error.runtimeError(ErrorCode.U_S0500, "StatemntSchema");
        }
    }
