    private void commonInitializer(File file) throws IOException {
        if (!file.getParentFile().canWrite()) {
            String msg = Logging.getMessage("generic.FolderNoWritePermission", file.getParentFile().getAbsolutePath());
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }
        this.targetFile = new RandomAccessFile(file, "rw");
        this.theChannel = this.targetFile.getChannel();
    }
