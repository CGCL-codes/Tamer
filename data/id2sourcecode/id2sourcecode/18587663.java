    private void writeQueuedReleases() throws IOException {
        for (int i = releaseQueue.size(); i > 0; ) {
            --i;
            QueuedRelease r = (QueuedRelease) releaseQueue.get(i);
            writeRequest(r.internal, r.objectId, r.type, r.method, r.threadId, null, false);
            releaseQueue.remove(i);
        }
    }
