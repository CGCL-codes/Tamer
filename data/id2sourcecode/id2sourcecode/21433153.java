    @Override
    public OutputStream createNewOutputStream(String path, long lastModifiedTime) throws IOException {
        String fullPath = pathPrefix + path;
        if (seenEntries.contains(fullPath)) {
            return new NullOutputStream();
        }
        seenEntries.add(fullPath);
        mkzipDirs(getParentPath(fullPath));
        ZipEntry zipEntry = new ZipEntry(fullPath);
        if (normalizeTimestamps) {
            zipEntry.setTime(0);
        } else if (lastModifiedTime >= 0) {
            zipEntry.setTime(lastModifiedTime);
        }
        jar.putNextEntry(zipEntry);
        return new OutputStreamOnJarEntry();
    }
