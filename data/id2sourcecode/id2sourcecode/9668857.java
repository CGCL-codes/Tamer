    private void printLogs() {
        for (LogComponent component : LogComponent.values) {
            for (LogWriter writer : component.AsyncWriters) {
                Collection<LogRow> logs = writer.pullLogs();
                WriterThread child = new WriterThread(writer, logs);
                child.start();
            }
        }
    }
