    @VisibleForPackageGroup
    public void save(@NotNull LuceneIndex index) {
        Util.checkNotNull(index);
        writeLock.lock();
        try {
            File indexDir = index.getIndexDirPath().getCanonicalFile();
            indexDir.mkdirs();
            File serFile = new File(indexDir, SER_FILENAME);
            if (serFile.exists() && !serFile.canWrite()) return;
            ObjectOutputStream out = null;
            try {
                serFile.createNewFile();
                FileOutputStream fout = new FileOutputStream(serFile);
                FileLock lock = fout.getChannel().lock();
                try {
                    out = new ObjectOutputStream(fout);
                    out.writeObject(index);
                } finally {
                    lock.release();
                }
            } catch (IOException e) {
                Util.printErr(e);
            } finally {
                Closeables.closeQuietly(out);
            }
            indexes.put(index, serFile.lastModified());
        } finally {
            writeLock.unlock();
        }
    }
