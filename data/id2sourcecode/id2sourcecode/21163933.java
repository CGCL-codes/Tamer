    public FileStore(File directory, Logger logger) throws FileStoreException {
        final File rootDirectory = directory.getAbsoluteFile();
        m_logger = logger;
        if (rootDirectory.exists()) {
            if (!rootDirectory.isDirectory()) {
                throw new FileStoreException("Could not write to directory '" + rootDirectory + "' as file with that name already exists");
            }
            if (!rootDirectory.canWrite()) {
                throw new FileStoreException("Could not write to directory '" + rootDirectory + "'");
            }
        }
        m_readmeFile = new File(rootDirectory, "README.txt");
        try {
            m_incomingDirectory = new Directory(new File(rootDirectory, "incoming"));
            m_currentDirectory = new Directory(new File(rootDirectory, "current"));
        } catch (Directory.DirectoryException e) {
            throw new FileStoreException(e.getMessage(), e);
        }
        m_incremental = false;
    }
