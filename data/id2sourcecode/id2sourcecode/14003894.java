    public boolean writeNextIO() {
        ThreadedChunkLoaderPending threadedchunkloaderpending = null;
        synchronized (chunkSaveLock) {
            if (pendingChunkList.size() > 0) {
                threadedchunkloaderpending = (ThreadedChunkLoaderPending) pendingChunkList.remove(0);
                pendingChunkCoords.remove(threadedchunkloaderpending.field_40739_a);
            } else {
                return false;
            }
        }
        if (threadedchunkloaderpending != null) {
            try {
                writeChunk(threadedchunkloaderpending);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return true;
    }
