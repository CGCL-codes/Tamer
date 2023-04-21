    private boolean isInProcess(ChannelRecord record) {
        boolean found = false;
        synchronized (workers) {
            Iterator<WorkerThread> i = workers.iterator();
            while (!found && i.hasNext()) {
                WorkerThread worker = i.next();
                found = worker.getChannelInProcess() == record;
            }
        }
        return found;
    }
