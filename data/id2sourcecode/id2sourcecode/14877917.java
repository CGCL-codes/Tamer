    public Object runWriteTransaction(ITransaction op) throws DbException {
        checkOpen();
        if (writable) {
            return runTransaction(op, TransactionMode.WRITE);
        } else {
            throw new DbException(DbErrorCode.MISUSE, "Can't start write transaction on read-only database");
        }
    }
