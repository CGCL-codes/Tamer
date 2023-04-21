    protected void getWorkersRecursive(String dir, ArrayList work) throws FileSystemException, IOException, CMSException {
        if (session.global.hasPermission(READ_PERMISSION, dir)) {
            ZipEntry entry = new ZipEntry(dir.substring(1));
            entry.setMethod(entry.STORED);
            entry.setCompressedSize(0);
            entry.setCrc(0);
            entry.setSize(0);
            entry.setTime(source.getLastModified(dir).getTime());
            zip.putNextEntry(entry);
            zip.closeEntry();
            String[] fs = source.listFiles(dir);
            String[] ids = source.listDirectories(dir);
            for (int i = 0; i < fs.length; i++) {
                getFileWorker(fs[i], work);
            }
            for (int i = 0; i < ids.length; i++) {
                getWorkersRecursive(ids[i], work);
            }
        }
    }
