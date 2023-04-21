    Cursor openCursor(Database db, CursorConfig cursorConfig, boolean writeCursor, Transaction txn) throws DatabaseException {
        if (cdbMode) {
            CdbCursors cdbCursors = null;
            WeakHashMap cdbCursorsMap = (WeakHashMap) localCdbCursors.get();
            if (cdbCursorsMap == null) {
                cdbCursorsMap = new WeakHashMap();
                localCdbCursors.set(cdbCursorsMap);
            } else {
                cdbCursors = (CdbCursors) cdbCursorsMap.get(db);
            }
            if (cdbCursors == null) {
                cdbCursors = new CdbCursors();
                cdbCursorsMap.put(db, cdbCursors);
            }
            List cursors;
            CursorConfig cdbConfig;
            if (writeCursor) {
                if (cdbCursors.readCursors.size() > 0) {
                    throw new IllegalStateException("cannot open CDB write cursor when read cursor is open");
                }
                cursors = cdbCursors.writeCursors;
                cdbConfig = new CursorConfig();
                DbCompat.setWriteCursor(cdbConfig, true);
            } else {
                cursors = cdbCursors.readCursors;
                cdbConfig = null;
            }
            Cursor cursor;
            if (cursors.size() > 0) {
                Cursor other = ((Cursor) cursors.get(0));
                cursor = other.dup(false);
            } else {
                cursor = db.openCursor(null, cdbConfig);
            }
            cursors.add(cursor);
            return cursor;
        } else {
            return db.openCursor(txn, cursorConfig);
        }
    }
