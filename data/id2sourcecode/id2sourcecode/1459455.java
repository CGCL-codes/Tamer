    @Override
    public void putNextEntry(ZipEntry entry) throws IOException, TaskTimeoutException {
        currentFile = entry.getName();
        if (!isSkip(entry.getName())) {
            checkTimeout();
            getOutStream().putNextEntry(entry);
            entryOpen = true;
        }
    }
