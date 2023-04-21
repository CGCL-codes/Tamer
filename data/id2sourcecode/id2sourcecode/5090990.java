    private String getLogSummary() {
        StringWriter out = new StringWriter();
        PrintWriter writer = new PrintWriter(out);
        if (logFile.length() > LogReader.MAX_FILE_LENGTH) {
            readLargeFileWithMonitor(writer);
        } else {
            readFileWithMonitor(writer);
        }
        writer.close();
        return out.toString();
    }
