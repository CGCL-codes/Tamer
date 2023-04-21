    private Statement compileLock() {
        read();
        readThis(Tokens.TABLE);
        OrderedHashSet readSet = new OrderedHashSet();
        OrderedHashSet writeSet = new OrderedHashSet();
        outerloop: while (true) {
            Table table = readTableName();
            switch(token.tokenType) {
                case Tokens.READ:
                    read();
                    readSet.add(table.getName());
                    break;
                case Tokens.WRITE:
                    read();
                    writeSet.add(table.getName());
                    break;
                default:
                    throw unexpectedToken();
            }
            if (token.tokenType == Tokens.COMMA) {
                read();
                continue;
            }
            break outerloop;
        }
        HsqlName[] writeTableNames = new HsqlName[writeSet.size()];
        writeSet.toArray(writeTableNames);
        readSet.removeAll(writeTableNames);
        HsqlName[] readTableNames = new HsqlName[readSet.size()];
        readSet.toArray(readTableNames);
        Statement cs = new StatementSession(StatementTypes.TRANSACTION_LOCK_TABLE, readTableNames, writeTableNames);
        return cs;
    }
