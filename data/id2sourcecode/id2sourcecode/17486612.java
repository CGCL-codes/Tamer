    private void generateReport(Map<String, String> results, String product, String config) throws IOException {
        generateHeader(results);
        StringBuilder line = new StringBuilder();
        line.append(product).append(",").append(config);
        for (String key : results.keySet()) {
            line.append(",").append(results.get(key));
        }
        writeLine(line.toString());
        for (ReportDesc reportDesc : reportDescs) {
            long readsPerSec = (long) Double.parseDouble(results.get("READS_PER_SEC"));
            long noReads = Long.parseLong(results.get("READ_COUNT"));
            long writesPerSec = (long) Double.parseDouble(results.get("WRITES_PER_SEC"));
            long noWrites = Long.parseLong(results.get("WRITE_COUNT"));
            reportDesc.updateData(product, config, readsPerSec, noReads, writesPerSec, noWrites);
        }
    }
