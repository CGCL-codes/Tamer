    private File getRepoDir() throws RepositoryAccessException {
        if (!this.isConfigured) throw new RepositoryAccessException("Repository is not configured.");
        File dir = new File(this.fileSystemPath);
        if (!dir.isAbsolute()) this.fileSystemPath = dir.getAbsolutePath();
        if (!dir.exists()) throw new RepositoryAccessException("Directory does not exist: " + this.fileSystemPath);
        if (!dir.isDirectory()) throw new RepositoryAccessException("Not a directory: " + this.fileSystemPath);
        if (!(dir.canRead() && dir.canWrite())) throw new RepositoryAccessException("Must be able to read and write to " + this.fileSystemPath);
        return dir;
    }
