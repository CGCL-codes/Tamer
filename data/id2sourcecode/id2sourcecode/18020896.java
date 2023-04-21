    public void execute() {
        if (null == davUri) {
            logger.debug("Skipping retreival of web resource. The framework.webdav.uri property is unset.");
            return;
        }
        final Project p = new Project();
        File lockFile = new File(destFile.getAbsolutePath() + ".lock");
        File newDestFile = new File(destFile.getAbsolutePath() + ".new");
        final URL fileUrl;
        try {
            fileUrl = makeUrl(depot.getName(), davPath);
        } catch (MalformedURLException e) {
            throw new CtlException("Input data for URL formulation caused an error. " + "Params: depot=" + depot.getName() + ", davUri=" + davUri + ", davPath=" + davPath, e);
        }
        try {
            final Task task = createTask(fileUrl, newDestFile, davUsername, davPassword);
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
                                throw new CtlException("Unable to remove dest file on windows: " + destFile);
                            }
                            if (!newDestFile.renameTo(destFile)) {
                                throw new CtlException("Unable to move temp file to dest file on windows: " + newDestFile + ", " + destFile);
                            }
                        } else {
                            throw new CtlException("Unable to move temp file to dest file: " + newDestFile + ", " + destFile);
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
            throw new CtlException("Unable to get and write deployments properties file: " + e.getMessage(), e);
        }
    }
