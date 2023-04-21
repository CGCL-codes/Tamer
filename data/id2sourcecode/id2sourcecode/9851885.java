    public BinaryFileDataService(IDataSourceDescriptor dataSourceDescriptor, Object serviceIdentity, String dbDirectoryPath) {
        super(dataSourceDescriptor, serviceIdentity);
        try {
            mDirectoryFile = new File(dbDirectoryPath);
            if (!mDirectoryFile.exists()) mDirectoryFile.mkdir();
            if (mDirectoryFile == null) Log.printError(this, "Failed to initialize FileDataService, path not found \"" + dbDirectoryPath + "\"!"); else if (!mDirectoryFile.canRead() || !mDirectoryFile.canWrite()) {
                Log.printError(this, "Failed to initialize FileDataService, no read/write access on \"" + dbDirectoryPath + "\"!");
                mDirectoryFile = null;
            } else Log.print(this, "FileDataService initialized on path \"" + dbDirectoryPath + "\".");
        } catch (Exception e) {
            Log.printError(this, "Failed to initialize FileDataService for path \"" + dbDirectoryPath + "\"!", e);
            mDirectoryFile = null;
        }
    }
