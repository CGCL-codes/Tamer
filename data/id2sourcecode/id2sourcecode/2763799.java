    Cursor dupCursor(Cursor cursor, boolean writeCursor, boolean samePosition) throws DatabaseException {
        if (cdbMode) {
            WeakHashMap cdbCursorsMap = (WeakHashMap) localCdbCursors.get();
            if (cdbCursorsMap != null) {
                Database db = cursor.getDatabase();
                CdbCursors cdbCursors = (CdbCursors) cdbCursorsMap.get(db);
                if (cdbCursors != null) {
                    List cursors = writeCursor ? cdbCursors.writeCursors : cdbCursors.readCursors;
                    if (cursors.contains(cursor)) {
                        Cursor newCursor = cursor.dup(samePosition);
                        cursors.add(newCursor);
                        return newCursor;
                    }
                }
            }
            throw new IllegalStateException("cursor to dup not tracked");
        } else {
            return cursor.dup(samePosition);
        }
    }
