    private Object performFilesDigestOperation(String filesDigestAlgo) throws NoSuchAlgorithmException, IOException {
        addDetailMessage("files digest algorithm: " + filesDigestAlgo);
        boolean isSupportedFilesDigestAlgo = false;
        for (String supportedFilesDigestAlgo : SUPPORTED_FILES_DIGEST_ALGOS) {
            if (supportedFilesDigestAlgo.equals(filesDigestAlgo)) {
                isSupportedFilesDigestAlgo = true;
                break;
            }
        }
        if (false == isSupportedFilesDigestAlgo) {
            throw new SecurityException("files digest algo not supported: " + filesDigestAlgo);
        }
        MessageDigest messageDigest = MessageDigest.getInstance(filesDigestAlgo);
        setStatusMessage(Status.NORMAL, MESSAGE_ID.SELECT_FILES);
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        int returnCode = fileChooser.showDialog(getParentComponent(), this.messages.getMessage(MESSAGE_ID.SELECT_FILES));
        if (JFileChooser.APPROVE_OPTION != returnCode) {
            throw new RuntimeException("file selection aborted");
        }
        setStatusMessage(Status.NORMAL, MESSAGE_ID.DIGESTING_FILES);
        FileDigestsDataMessage fileDigestsDataMessage = new FileDigestsDataMessage();
        fileDigestsDataMessage.fileDigestInfos = new LinkedList<String>();
        File[] selectedFiles = fileChooser.getSelectedFiles();
        long totalSize = 0;
        for (File selectedFile : selectedFiles) {
            totalSize += selectedFile.length();
        }
        final int BUFFER_SIZE = 1024 * 10;
        int progressMax = (int) (totalSize / BUFFER_SIZE);
        this.view.resetProgress(progressMax);
        addDetailMessage("total data size to digest: " + (totalSize / 1024) + " KiB");
        for (File selectedFile : selectedFiles) {
            fileDigestsDataMessage.fileDigestInfos.add(filesDigestAlgo);
            long fileSize = selectedFile.length();
            addDetailMessage(selectedFile.getAbsolutePath() + ": " + (fileSize / 1024) + " KiB");
            FileInputStream fileInputStream = new FileInputStream(selectedFile);
            DigestInputStream digestInputStream = new DigestInputStream(fileInputStream, messageDigest);
            byte[] buffer = new byte[BUFFER_SIZE];
            while (-1 != digestInputStream.read(buffer)) {
                this.view.increaseProgress();
            }
            digestInputStream.close();
            byte[] fileDigestValue = messageDigest.digest();
            messageDigest.reset();
            String fileDigest = toHex(fileDigestValue);
            fileDigestsDataMessage.fileDigestInfos.add(fileDigest);
            fileDigestsDataMessage.fileDigestInfos.add(selectedFile.getName());
        }
        this.view.setProgressIndeterminate();
        Object resultMessage = sendMessage(fileDigestsDataMessage);
        return resultMessage;
    }
