    boolean isCDBCursorOpen(Database db) throws DatabaseException {
        if (cdbMode) {
            WeakHashMap cdbCursorsMap = (WeakHashMap) localCdbCursors.get();
            if (cdbCursorsMap != null) {
                CdbCursors cdbCursors = (CdbCursors) cdbCursorsMap.get(db);
                if (cdbCursors != null && (cdbCursors.readCursors.size() > 0 || cdbCursors.writeCursors.size() > 0)) {
                    return true;
                }
            }
        }
        return false;
    }
