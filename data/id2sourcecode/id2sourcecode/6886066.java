    public FileSystemItem[] list(String path, Boolean includeFiles) throws IOException {
        SFTPv3Client channel = getChannel();
        SFTPv3FileAttributes stat = channel.lstat(path);
        if (stat.isDirectory()) return getChildren(path, includeFiles);
        return null;
    }
