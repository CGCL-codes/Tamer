    private boolean writeRequest(boolean internal, String oid, TypeDescription type, String function, ThreadId tid, Object[] arguments) throws IOException {
        IMethodDescription desc = type.getMethodDescription(function);
        synchronized (output) {
            if (desc.getIndex() == MethodDescription.ID_RELEASE && releaseQueue.size() < MAX_RELEASE_QUEUE_SIZE) {
                releaseQueue.add(new QueuedRelease(internal, oid, type, desc, tid));
                return false;
            } else {
                writeQueuedReleases();
                return writeRequest(internal, oid, type, desc, tid, arguments, true);
            }
        }
    }
