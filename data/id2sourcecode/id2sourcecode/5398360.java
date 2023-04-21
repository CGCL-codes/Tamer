    OperationStatus append(Object value, Object[] retPrimaryKey, Object[] retValue) throws DatabaseException {
        DatabaseEntry keyThang = new DatabaseEntry();
        DatabaseEntry valueThang = new DatabaseEntry();
        useValue(value, valueThang, null);
        OperationStatus status;
        if (keyAssigner != null) {
            keyAssigner.assignKey(keyThang);
            if (!range.check(keyThang)) {
                throw new IllegalArgumentException("assigned key out of range");
            }
            DataCursor cursor = new DataCursor(this, true);
            try {
                status = cursor.getCursor().putNoOverwrite(keyThang, valueThang);
            } finally {
                cursor.close();
            }
        } else {
            if (currentTxn.isCDBCursorOpen(db)) {
                throw new IllegalStateException("cannot open CDB write cursor when read cursor is open");
            }
            status = DbCompat.append(db, useTransaction(), keyThang, valueThang);
            if (status == OperationStatus.SUCCESS && !range.check(keyThang)) {
                db.delete(useTransaction(), keyThang);
                throw new IllegalArgumentException("appended record number out of range");
            }
        }
        if (status == OperationStatus.SUCCESS) {
            returnPrimaryKeyAndValue(keyThang, valueThang, retPrimaryKey, retValue);
        }
        return status;
    }
