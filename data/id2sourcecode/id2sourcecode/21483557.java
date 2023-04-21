    private synchronized PackedOffsets putFileSummary(TrackedFileSummary tfs) throws DatabaseException {
        if (env.isReadOnly()) {
            throw EnvironmentFailureException.unexpectedState("Cannot write file summary in a read-only environment");
        }
        if (tfs.isEmpty()) {
            return null;
        }
        if (!cachePopulated) {
            return null;
        }
        long fileNum = tfs.getFileNumber();
        Long fileNumLong = Long.valueOf(fileNum);
        FileSummary summary = fileSummaryMap.get(fileNumLong);
        if (summary == null) {
            if (!fileSummaryMap.isEmpty() && fileNum < fileSummaryMap.lastKey() && !env.getFileManager().isFileValid(fileNum)) {
                env.getLogManager().removeTrackedFile(tfs);
                return null;
            }
            summary = new FileSummary();
        }
        FileSummary tmp = new FileSummary();
        tmp.add(summary);
        tmp.add(tfs);
        int sequence = tmp.getEntriesCounted();
        FileSummaryLN ln = new FileSummaryLN(env, summary);
        ln.setTrackedSummary(tfs);
        insertFileSummary(ln, fileNum, sequence);
        summary = ln.getBaseSummary();
        if (fileSummaryMap.put(fileNumLong, summary) == null) {
            MemoryBudget mb = env.getMemoryBudget();
            mb.updateAdminMemoryUsage(MemoryBudget.UTILIZATION_PROFILE_ENTRY);
        }
        return ln.getObsoleteOffsets();
    }
