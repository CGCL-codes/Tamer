    private void updateUsageStats(long readByteCount, long writeByteCount, long startTime, long endTime) {
        mDataUsed = readByteCount + writeByteCount;
        mStart.setTimeInMillis(startTime);
        mEnd.setTimeInMillis(endTime);
        updateUI();
    }
