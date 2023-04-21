    private void internalInit(final String[] fileList, final String[] remappedFileList) throws Exception {
        int filtersCount = 0;
        final ProcessorInfo processorInfo = new ProcessorInfo();
        final long sTime = System.nanoTime();
        if (!FdtMain.isIsServerMode()) guiFileStatus = GUIFileStatus.getGUIFileStatusInstance();
        if (role == SERVER && config.getRestrictedTransferDirectories() != null) {
            for (String name : fileList) {
                if (config.authorizedTransfer(new File(name)) == false) throw new IOException("Unauthorized download request for '" + name + "' from server.");
            }
        }
        try {
            final String preProcessFiltersProp = config.getPreFilters();
            if (preProcessFiltersProp == null || preProcessFiltersProp.length() == 0) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.log(Level.FINE, "No FDT Preprocess Filters defined");
                }
            } else {
                String[] preProcessFilters = preProcessFiltersProp.split(",");
                if (preProcessFilters == null || preProcessFilters.length == 0) {
                    logger.log(Level.WARNING, "Cannot understand -preFilters option!");
                } else {
                    filtersCount = preProcessFilters.length;
                    processorInfo.fileList = new String[fileList.length];
                    processorInfo.destinationDir = (this.remoteDir == null) ? config.getDestinationDir() : this.remoteDir;
                    System.arraycopy(fileList, 0, processorInfo.fileList, 0, fileList.length);
                    for (final String filterName : preProcessFilters) {
                        Preprocessor preprocessor = (Preprocessor) (Class.forName(filterName).newInstance());
                        preprocessor.preProcessFileList(processorInfo, this.controlChannel.subject);
                    }
                }
            }
        } finally {
            StringBuilder sb = new StringBuilder();
            if (filtersCount > 0) {
                sb.append("[ FDTReaderSession ] Preprocessing: ").append(filtersCount).append(" filters in ").append(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - sTime)).append(" ms");
            } else {
                sb.append("[ FDTReaderSession ] No pre processing filters defined/processed.");
            }
            logger.log(Level.INFO, sb.toString());
        }
        Map<String, String> initialMapping = new HashMap<String, String>();
        List<String> newFileList = new ArrayList<String>();
        Map<String, String> newRemappedFileList = new HashMap<String, String>();
        if (filtersCount > 0) {
            this.processorInfo = processorInfo;
            this.remoteDir = processorInfo.destinationDir;
            newFileList = new ArrayList<String>(processorInfo.fileList.length);
            newFileList.addAll(Arrays.asList(processorInfo.fileList));
        } else {
            if (recursive) {
                final int len = fileList.length;
                for (int iter = 0; iter < len; iter++) {
                    final String fName = fileList[iter];
                    final String remappedFName = (remappedFileList == null) ? null : remappedFileList[iter];
                    List<String> tmpFL = new ArrayList<String>();
                    List<String> tmpRFL = new ArrayList<String>();
                    Utils.getRecursiveFiles(fName, remappedFName, tmpFL, tmpRFL);
                    if (!isFileList) {
                        for (String ffName : tmpFL) {
                            String parent = initialMapping.get(ffName);
                            if (parent != null) {
                                if (fName.length() > parent.length()) {
                                    parent = fName;
                                }
                            } else {
                                parent = fName;
                            }
                            if (new File(parent).isDirectory()) {
                                initialMapping.put(new File(ffName).getAbsolutePath(), new File(parent).getAbsolutePath());
                            }
                        }
                    }
                    newFileList.addAll(tmpFL);
                    int c = 0;
                    for (String fname : tmpFL) {
                        newRemappedFileList.put(fName, tmpRFL.get(c++));
                    }
                }
            } else {
                newFileList = Arrays.asList(fileList);
                int c = 0;
                if (remappedFileList != null) {
                    for (String f : newFileList) {
                        newRemappedFileList.put(f, remappedFileList[c++]);
                    }
                } else {
                    newRemappedFileList = null;
                }
            }
        }
        final FileChannelProvider fcp = Config.getInstance().getFileChannelProviderFactory().newReaderFileChannelProvider(this);
        int count = 0;
        for (final String fName : newFileList) {
            logger.log("Starting Filereader session for " + fName);
            FileReaderSession frs = new FileReaderSession(fName, this, isLoop, fcp);
            fileSessions.put(frs.sessionID, frs);
            setSessionSize(sessionSize() + frs.sessionSize());
            if (!FdtMain.isIsServerMode()) {
                guiFileStatus.initFileInfo(fName, frs.sessionID.toString(), fName, "Server", frs.sessionSize);
                guiFileStatus.initUploadTable(fName, "Server", frs.sessionID().toString(), frs.sessionSize);
            }
            count++;
        }
        buildPartitionMap();
        int size = partitionsMap.size();
        if (size == 0) {
            if (!FdtMain.isIsServerMode()) ShowMessageDialog.showErrorDialog("A server error occurred, please alert the server administrator and send the log file.", "Server Error");
            throw new FDTProcolException("\n\nERROR: Cannot identify partition map for the specified fileList: " + Arrays.toString(fileList) + " No such file or directory ??");
        }
        sendRemoteSessions(initialMapping, newRemappedFileList);
    }
