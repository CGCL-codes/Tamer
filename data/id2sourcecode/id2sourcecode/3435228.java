    public WorkspaceManager(File workspaceDir) throws IOException {
        this.workspaceDir = workspaceDir;
        lock = new File(workspaceDir, "jpatch.lock");
        if (!workspaceDir.exists()) {
            try {
                if (!workspaceDir.mkdirs()) {
                    throw new IOException("Can't reate workspace directory \"" + workspaceDir.getCanonicalPath() + "\".");
                }
            } catch (SecurityException e) {
                throw new IOException("Can't create workspace directory \"" + workspaceDir.getCanonicalPath() + "\": " + e.getMessage());
            }
        }
        if (!lock.exists()) {
            try {
                lock.createNewFile();
            } catch (IOException e) {
                throw new IOException("Can't create lock in workspace directory \"" + workspaceDir.getCanonicalPath() + "\": " + e.getMessage());
            }
        }
        if (new FileOutputStream(lock).getChannel().tryLock() == null) {
            throw new IOException("Can't acquire exclusive lock on workspace \"" + workspaceDir.getCanonicalPath() + "\".");
        }
    }
