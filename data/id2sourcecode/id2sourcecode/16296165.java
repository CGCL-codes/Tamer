    public boolean close() {
        boolean closed = super.close();
        if (closed && runner != null && runner.getChannel() != null && runner.getChannel().isRunning() && runner.getChannel().getTaskMonitor() != null) {
            runner.getChannel().getTaskMonitor().setCancelRequested();
        }
        return closed;
    }
