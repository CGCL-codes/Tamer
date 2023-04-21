    public void copyTo(String path) throws IOException {
        DedupFileChannel ch = null;
        File dest = new File(path + File.separator + "ddb" + File.separator + this.GUID.substring(0, 2) + File.separator + this.GUID);
        dest.mkdirs();
        try {
            ch = this.getChannel();
            this.writeCache();
            this.sync();
            this.writeBufferLock.lock();
            bdb.copy(dest.getPath() + File.separator + this.GUID + ".map");
            chunkStore.copy(dest.getPath() + File.separator + this.GUID + ".chk");
        } catch (Exception e) {
            SDFSLogger.getLog().warn("unable to copy to" + mf.getPath(), e);
            throw new IOException("unable to clone file " + mf.getPath(), e);
        } finally {
            this.writeBufferLock.unlock();
            if (ch != null) ch.close();
            ch = null;
        }
    }
