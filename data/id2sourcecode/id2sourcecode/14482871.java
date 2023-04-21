    private final synchronized void putNextEntry(ZipEntry entry) throws IOException, FileNotFoundException {
        if (this.entryOpen) throw new IllegalStateException("previous entry not closed");
        this.entryOpen = true;
        this.getOutStream().putNextEntry(entry);
    }
