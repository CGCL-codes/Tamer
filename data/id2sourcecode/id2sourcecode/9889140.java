    private int getPuzzleNumber(Cursor cursor, long puzzleId, int fromNumber, int toNumber) {
        if (fromNumber > toNumber) return -1;
        int candidate = (fromNumber + toNumber) / 2;
        if (!cursor.moveToPosition(candidate)) throw new IllegalStateException();
        int id = cursor.getInt(0);
        if (id == puzzleId) return candidate;
        if (id < puzzleId) return getPuzzleNumber(cursor, puzzleId, candidate + 1, toNumber); else return getPuzzleNumber(cursor, puzzleId, fromNumber, candidate - 1);
    }
