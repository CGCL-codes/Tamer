    public DedupFile snapshot(MetaDataDedupFile snapmf) throws IOException, HashtableFullException {
        DedupFileChannel ch = null;
        try {
            ch = this.getChannel();
            this.writeCache();
            this.sync();
            SparseDedupFile _df = new SparseDedupFile(snapmf);
            this.writeBufferLock.lock();
            bdb.copy(_df.getDatabasePath());
            chunkStore.copy(_df.chunkStorePath);
            return _df;
        } catch (Exception e) {
            SDFSLogger.getLog().warn("unable to clone file " + mf.getPath(), e);
            throw new IOException("unable to clone file " + mf.getPath(), e);
        } finally {
            this.writeBufferLock.unlock();
            if (ch != null) ch.close();
            ch = null;
        }
    }
