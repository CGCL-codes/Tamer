    public void updateData(String product, String config, long readsPerSec, long noReads, long writesPerSec, long noWrites) {
        if (includeAll) {
            ReportItem item = new ReportItem(product, config);
            updateItem(readsPerSec, noReads, writesPerSec, noWrites, item);
            items.add(item);
        } else {
            for (ReportItem item : items) {
                if (item.matches(product, config)) {
                    updateItem(readsPerSec, noReads, writesPerSec, noWrites, item);
                }
            }
        }
    }
