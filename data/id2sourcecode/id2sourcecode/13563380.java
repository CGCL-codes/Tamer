    private void updateItem(long readsPerSec, long noReads, long writesPerSec, long noWrites, ReportItem item) {
        item.setReadsPerSec(readsPerSec);
        item.setNoReads(noReads);
        item.setWritesPerSec(writesPerSec);
        item.setNoWrites(noWrites);
    }
