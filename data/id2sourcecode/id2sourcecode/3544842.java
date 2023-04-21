    DbFile(final DbFileSystem fileSystem, final RandomAccessFile file, final File filePath, final FileType fileType, final Set<FileOpenPermission> permissions, final boolean noLock) {
        this.file = file;
        this.filePath = filePath;
        this.filePathResolved = filePath.getAbsolutePath();
        this.fileType = fileType;
        this.permissions = EnumSet.copyOf(permissions);
        this.noLock = noLock;
        this.fileLockManager = new DbFileLockManager(this.filePathResolved, file.getChannel());
        findLockInfo();
        OSTRACE("OPEN    %s\n", this.filePath);
    }
