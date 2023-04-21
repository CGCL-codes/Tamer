    public void execute() {
        final Project p = new Project();
        File lockFile = new File(destFile.getAbsolutePath() + ".lock");
        File newDestFile = new File(destFile.getAbsolutePath() + ".new");
        try {
            final Task task = createTask(fileUrl, newDestFile, username, password);
            task.setProject(p);
            FileChannel channel = new RandomAccessFile(lockFile, "rw").getChannel();
            FileLock lock = channel.lock();
            try {
                synchronized (GetDeploymentsFile.class) {
                    int c = 0;
                    FileUtils.copyFileStreams(destFile, newDestFile);
                    newDestFile.setLastModified(destFile.lastModified());
                    task.execute();
                    String osName = System.getProperty("os.name");
                    if (!newDestFile.renameTo(destFile)) {
                        if (osName.toLowerCase().indexOf("windows") > -1 && destFile.exists()) {
                            if (!destFile.delete()) {
                                throw new AntdepoException("Unable to remove dest file on windows: " + destFile);
                            }
                            if (!newDestFile.renameTo(destFile)) {
                                throw new AntdepoException("Unable to move temp file to dest file on windows: " + newDestFile + ", " + destFile);
                            }
                        } else {
                            throw new AntdepoException("Unable to move temp file to dest file: " + newDestFile + ", " + destFile);
                        }
                    }
                }
            } finally {
                lock.release();
                channel.close();
            }
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
            e.printStackTrace(System.err);
            throw new AntdepoException("Unable to get and write deployments properties file: " + e.getMessage(), e);
        }
    }
