    private void mkzipDirs(String path) throws IOException {
        if (path == null) {
            return;
        }
        if (createdDirs.contains(path)) {
            return;
        }
        mkzipDirs(getParentPath(path));
        ZipEntry entry = new ZipEntry(path + '/');
        entry.setSize(0);
        entry.setCompressedSize(0);
        entry.setCrc(0);
        entry.setMethod(ZipOutputStream.STORED);
        if (normalizeTimestamps) {
            entry.setTime(0);
        }
        jar.putNextEntry(entry);
        createdDirs.add(path);
    }
