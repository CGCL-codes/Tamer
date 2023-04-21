    protected SuspectFilesComposite moveFileIntoIncomingDirectory(File pListFile, String pIncomingDirectoryPath) throws IOException {
        String methodSignature = "void dumpFileIntoIncomingDirectory(File,String): ";
        long fileTimestamp = System.nanoTime();
        File incomingDirectory = new File(pIncomingDirectoryPath);
        if (!incomingDirectory.exists()) {
            FileUtils.forceMkdir(incomingDirectory);
        }
        FileUtils.copyFileToDirectory(pListFile, incomingDirectory);
        FileUtils.forceDelete(pListFile);
        String fileName = pListFile.getName();
        String incomingQualifiedFilePath = pIncomingDirectoryPath + "/" + fileName;
        SuspectFilesComposite sfc = new SuspectFilesComposite();
        String timeQualifiedProcessingFileName = null;
        sfc.setIncomingFileName(fileName);
        File incomingQualifiedFile = new File(incomingQualifiedFilePath);
        if (incomingQualifiedFilePath.toUpperCase().endsWith(".ZIP")) {
            File actualQualifiedFile = uncompressAndSaveFile(incomingQualifiedFilePath, pIncomingDirectoryPath, fileTimestamp);
            FileUtils.forceDelete(incomingQualifiedFile);
            timeQualifiedProcessingFileName = pIncomingDirectoryPath + "/" + fileTimestamp + "_" + actualQualifiedFile.getName();
            if (isLoggingInfo()) {
                logInfo(methodSignature + ": Incoming Uncompressed suspect file final name: " + timeQualifiedProcessingFileName);
            }
            sfc.setProcessingFilePath(timeQualifiedProcessingFileName);
            return sfc;
        }
        timeQualifiedProcessingFileName = pIncomingDirectoryPath + "/" + fileTimestamp + "_" + fileName;
        if (isLoggingInfo()) {
            logInfo(methodSignature + ": Incoming suspect file final name: " + timeQualifiedProcessingFileName);
        }
        sfc.setProcessingFilePath(timeQualifiedProcessingFileName);
        incomingQualifiedFile.renameTo(new File(timeQualifiedProcessingFileName));
        return sfc;
    }
