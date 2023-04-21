    public void store(File localAreaDir, final JobDependency jobDependency, final TransferProgress progressObject) throws IOException {
        final String area = jobDependency.parseArea().toString();
        final String path = jobDependency.getPath();
        final String subpath = path.substring(area.length() + 1);
        if (subpath.length() != 0) {
            throw new UnsupportedOperationException("partial upload is not supported yet: " + jobDependency);
        }
        final File remoteJobDir = new File(remoteCatalogRootDir, BuildRepository.executionBase(jobDependency));
        final File remoteAreaBase = new File(remoteJobDir, area);
        remoteAreaBase.mkdirs();
        if (!remoteAreaBase.isDirectory()) {
            throw new IOException("Failed to create directory: " + remoteAreaBase);
        }
        LOGGER.fine(String.format("LocalJobTransfer.store %s", localAreaDir));
        final File incompleteUploadIndicator = new File(remoteAreaBase + ".part");
        FileUtils.fileWrite(incompleteUploadIndicator, "# presence of this file means that the upload did not finish yet");
        final File listOfFilesFile = new File(remoteAreaBase + BuildRepository.EXTENSION_FILELIST);
        final PrintWriter listOfFiles = new PrintWriter(listOfFilesFile);
        listOfFiles.println("# " + listOfFilesFile);
        final File listOfDirsFile = new File(remoteAreaBase + BuildRepository.EXTENSION_DIRLIST);
        final PrintWriter listOfDirs = new PrintWriter(listOfDirsFile);
        listOfDirs.println("# " + listOfDirsFile);
        final String srcPrefix = localAreaDir.getAbsolutePath() + '/';
        final File remoteContentProperties = new File(remoteAreaBase + BuildRepository.EXTENSION_PROPERTIES);
        final String comment = jobDependency.toString();
        final PushingDirectoryTraversal t = new PushingDirectoryTraversal(new FileFilter() {

            int fileCount = progressObject.getTransferredFiles();

            int dirCount = progressObject.getTransferredDirs();

            long byteCount = progressObject.getTransferredBytes();

            static final long PROPSAVE_BYTE_RATE = 100000;

            long nextpropsave = PROPSAVE_BYTE_RATE;

            public boolean accept(File src) {
                try {
                    final String absPath = src.getAbsolutePath().replace('\\', '/');
                    final String uri = absPath.substring(srcPrefix.length());
                    final File dest = new File(remoteAreaBase, uri);
                    if (src.isDirectory()) {
                        dest.mkdirs();
                        final AtomicInteger fileCnt = new AtomicInteger(0);
                        final AtomicInteger dirCnt = new AtomicInteger(0);
                        final AtomicInteger otherCnt = new AtomicInteger(0);
                        src.listFiles(new FileFilter() {

                            public boolean accept(File pathname) {
                                if (pathname.isDirectory()) {
                                    dirCnt.incrementAndGet();
                                } else if (pathname.isFile()) {
                                    fileCnt.incrementAndGet();
                                } else {
                                    otherCnt.incrementAndGet();
                                }
                                return false;
                            }
                        });
                        if (dirCnt.intValue() == 0) {
                            listOfDirs.println(uri + "/ " + dirCnt + " " + fileCnt + " " + otherCnt);
                            listOfDirs.flush();
                        }
                        dirCount++;
                        progressObject.setTransferredDirs(dirCount);
                    } else {
                        dest.getParentFile().mkdirs();
                        FileUtils.copyFile(src, dest);
                        listOfFiles.println(uri + " " + src.length());
                        listOfFiles.flush();
                        fileCount++;
                        progressObject.setTransferredFiles(fileCount);
                        byteCount += src.length();
                        progressObject.setTransferredBytes(byteCount);
                        if (byteCount > nextpropsave) {
                            saveProgress(remoteContentProperties, progressObject, "--- INCOMPLETE --- " + comment);
                            nextpropsave = byteCount + PROPSAVE_BYTE_RATE;
                        }
                    }
                } catch (IOException e) {
                    throw new IllegalArgumentException("error while storing " + src, e);
                }
                return false;
            }
        });
        t.setWantDirs(true);
        t.setWantFiles(true);
        t.setSorted(true);
        try {
            listOfFiles.println("# traversing " + localAreaDir);
            listOfFiles.flush();
            t.traverse(localAreaDir);
            listOfFiles.println("# done ");
            listOfFiles.flush();
            listOfDirs.println("# done ");
            listOfDirs.flush();
        } finally {
            listOfFiles.close();
            listOfDirs.close();
        }
        saveProgress(remoteContentProperties, progressObject, comment);
        if (listOfFiles.checkError()) {
            throw new IOException("failure in updating " + listOfFilesFile);
        }
        if (listOfDirs.checkError()) {
            throw new IOException("failure in updating " + listOfDirsFile);
        }
        incompleteUploadIndicator.delete();
    }
