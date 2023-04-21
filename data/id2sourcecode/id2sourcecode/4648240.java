    public void setDefaultWorkingDirectory(NodeName nodeName, File directory) throws IOException, XMLNodeDoesNotExistException {
        if (directory == null) {
            throw new IOException("The directory File is null.");
        }
        if (!directory.isDirectory()) {
            throw new IOException(directory.getAbsolutePath() + " is not a directory.");
        }
        if (!directory.canWrite()) {
            throw new IOException("Cannot write to " + directory.getAbsolutePath() + ". Directory is read-only.");
        }
        if (!directory.exists()) {
            directory.mkdir();
        }
        Stack oNameStack = getInvertedNodeNameStack(nodeName);
        Element elmProjectElement = melmDefaultDirectories;
        oNameStack.pop();
        while (!oNameStack.isEmpty()) {
            String sProj = (String) oNameStack.pop();
            elmProjectElement = getConfElement(elmProjectElement, sProj);
        }
        XMLUtil.addOrReplaceElementText(elmProjectElement, directory.getAbsolutePath());
        saveConfFile();
    }
