    public void resolve(Session session) {
        for (int i = 0; i < statements.length; i++) {
            if (statements[i].getType() == StatementTypes.LEAVE || statements[i].getType() == StatementTypes.ITERATE) {
                if (!findLabel((StatementSimple) statements[i])) {
                    throw Error.error(ErrorCode.X_42508, ((StatementSimple) statements[i]).label.name);
                }
                continue;
            }
            if (statements[i].getType() == StatementTypes.RETURN) {
                if (!root.isFunction()) {
                    throw Error.error(ErrorCode.X_42602, Tokens.T_RETURN);
                }
            }
        }
        for (int i = 0; i < statements.length; i++) {
            statements[i].resolve(session);
        }
        for (int i = 0; i < handlers.length; i++) {
            handlers[i].resolve(session);
        }
        OrderedHashSet writeTableNamesSet = new OrderedHashSet();
        OrderedHashSet readTableNamesSet = new OrderedHashSet();
        OrderedHashSet set = new OrderedHashSet();
        for (int i = 0; i < variables.length; i++) {
            set.addAll(variables[i].getReferences());
        }
        if (condition != null) {
            set.addAll(condition.getReferences());
            readTableNamesSet.addAll(condition.getTableNamesForRead());
        }
        for (int i = 0; i < statements.length; i++) {
            set.addAll(statements[i].getReferences());
            readTableNamesSet.addAll(statements[i].getTableNamesForRead());
            writeTableNamesSet.addAll(statements[i].getTableNamesForWrite());
        }
        for (int i = 0; i < handlers.length; i++) {
            set.addAll(handlers[i].getReferences());
            readTableNamesSet.addAll(handlers[i].getTableNamesForRead());
            writeTableNamesSet.addAll(handlers[i].getTableNamesForWrite());
        }
        readTableNamesSet.removeAll(writeTableNamesSet);
        readTableNames = new HsqlName[readTableNamesSet.size()];
        readTableNamesSet.toArray(readTableNames);
        writeTableNames = new HsqlName[writeTableNamesSet.size()];
        writeTableNamesSet.toArray(writeTableNames);
        references = set;
    }
