    private void addStat(int id, int submitTime, int dataId, int dataSize, int userId) throws SQLException {
        int runTime = MIN_RUNTIME + random.nextInt(MAX_RUNTIME - MIN_RUNTIME);
        stat.setInt(1, id);
        stat.setInt(2, submitTime);
        stat.setInt(3, runTime);
        stat.setInt(4, dataId);
        stat.setInt(5, dataSize);
        stat.setInt(6, userId);
        stat.addBatch();
    }
