    private synchronized PackedOffsets putFileSummary(TrackedFileSummary tfs) throws DatabaseException {
        if (env.isReadOnly()) {
            throw new DatabaseException("Cannot write file summary in a read-only environment");
        }
        if (tfs.isEmpty()) {
            return null;
        }
        if (!cachePopulated) {
            return null;
        }
        long fileNum = tfs.getFileNumber();
        Long fileNumLong = new Long(fileNum);
        FileSummary summary = (FileSummary) fileSummaryMap.get(fileNumLong);
        if (summary == null) {
            File file = new File(env.getFileManager().getFullFileName(fileNum, FileManager.JE_SUFFIX));
            if (!file.exists()) {
                return null;
            }
            summary = new FileSummary();
        }
        FileSummary tmp = new FileSummary();
        tmp.add(summary);
        tmp.add(tfs);
        int sequence = tmp.getEntriesCounted();
        FileSummaryLN ln = new FileSummaryLN(summary);
        ln.setTrackedSummary(tfs);
        insertFileSummary(ln, fileNum, sequence);
        summary = ln.getBaseSummary();
        if (fileSummaryMap.put(fileNumLong, summary) == null) {
            MemoryBudget mb = env.getMemoryBudget();
            mb.updateMiscMemoryUsage(MemoryBudget.UTILIZATION_PROFILE_ENTRY);
        }
        return ln.getObsoleteOffsets();
    }
