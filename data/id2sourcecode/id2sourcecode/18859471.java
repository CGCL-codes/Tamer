    public void execute() {
        final Project p = new Project();
        File lockFile = new File(destFile.getAbsolutePath() + ".lock");
        File newDestFile = new File(destFile.getAbsolutePath() + ".new");
        try {
            synchronized (GetLocalFile.class) {
                FileChannel channel = new RandomAccessFile(lockFile, "rw").getChannel();
                FileLock lock = channel.lock();
                try {
                    FileUtils.copyFileStreams(destFile, newDestFile);
                    newDestFile.setLastModified(destFile.lastModified());
                    FileUtils.copyFileStreams(sourceFile, newDestFile);
                    String osName = System.getProperty("os.name");
                    if (!newDestFile.renameTo(destFile)) {
                        if (osName.toLowerCase().indexOf("windows") > -1 && destFile.exists()) {
                            if (!destFile.delete()) {
                                throw new CtlException("Unable to remove dest file on windows: " + destFile);
                            }
                            if (!newDestFile.renameTo(destFile)) {
                                throw new CtlException("Unable to move temp file to dest file on windows: " + newDestFile + ", " + destFile);
                            }
                        } else {
                            throw new CtlException("Unable to move temp file to dest file: " + newDestFile + ", " + destFile);
                        }
                    }
                } finally {
                    lock.release();
                    channel.close();
                }
            }
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
            e.printStackTrace(System.err);
            throw new CtlException("Unable to get and write resources.properties file: " + e.getMessage(), e);
        }
    }
