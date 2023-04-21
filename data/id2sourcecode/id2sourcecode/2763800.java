    void closeCursor(Cursor cursor) throws DatabaseException {
        if (cursor == null) {
            return;
        }
        if (cdbMode) {
            WeakHashMap cdbCursorsMap = (WeakHashMap) localCdbCursors.get();
            if (cdbCursorsMap != null) {
                Database db = cursor.getDatabase();
                CdbCursors cdbCursors = (CdbCursors) cdbCursorsMap.get(db);
                if (cdbCursors != null) {
                    if (cdbCursors.readCursors.remove(cursor) || cdbCursors.writeCursors.remove(cursor)) {
                        cursor.close();
                        return;
                    }
                }
            }
            throw new IllegalStateException("closing CDB cursor that was not known to be open");
        } else {
            cursor.close();
        }
    }
