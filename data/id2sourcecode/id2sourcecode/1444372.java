    public boolean sync() throws IOException {
        if (safeSync) {
            try {
                this.lock.lock();
                raf = new RandomAccessFile(blockFile, "rw");
                raf.getChannel().force(false);
                raf.close();
                raf = null;
                return true;
            } catch (Exception e) {
                SDFSLogger.getLog().warn("unable to sync " + this.blockFile.getPath(), e);
                throw new IOException(e);
            } finally {
                this.lock.unlock();
            }
        }
        return false;
    }
