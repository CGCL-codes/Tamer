    public void optimize() throws HashtableFullException {
        DedupFileChannel ch = null;
        try {
            ch = this.getChannel();
            if (this.closed) {
                throw new IOException("file already closed");
            }
            if (!mf.isDedup()) {
                this.writeCache();
                this.checkForDups();
                this.chunkStore.close();
                new File(this.chunkStorePath).delete();
                this.chunkStore = new LargeLongByteArrayMap(chunkStorePath, (long) -1, Main.CHUNK_LENGTH);
            } else {
                this.pushLocalDataToChunkStore();
                this.chunkStore.vanish();
                this.chunkStore = null;
                this.chunkStore = new LargeLongByteArrayMap(chunkStorePath, (long) -1, Main.CHUNK_LENGTH);
            }
        } catch (IOException e) {
        } finally {
            try {
                ch.close();
            } catch (Exception e) {
            }
            ch = null;
        }
    }
