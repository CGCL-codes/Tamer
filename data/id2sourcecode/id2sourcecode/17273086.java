    public ScheduleCopyOperationThread(AbstractFile l_fromFolder, AbstractFile l_toFolder, boolean l_includeHiddenFiles, FileManager l_fileManager, boolean l_promptOnOverwrite) {
        this.fileManager = l_fileManager;
        this.fromFolder = l_fromFolder;
        this.includeHiddenFiles = l_includeHiddenFiles;
        this.promptOnOverwrite = l_promptOnOverwrite;
        this.toFolder = l_toFolder;
    }
