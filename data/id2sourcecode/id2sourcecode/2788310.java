    private static void addFile(SVNRepository repository, String filePath, String commitStr, byte[] data) throws SVNException {
        int lastSlash = filePath.lastIndexOf("/");
        if (lastSlash < 1) throw new IllegalArgumentException("Expected file name with directory path, got " + filePath);
        String dirPath = filePath.substring(0, lastSlash);
        addDirPath(repository, dirPath);
        SVNNodeKind nodeKind = repository.checkPath(filePath, -1);
        if (nodeKind == SVNNodeKind.DIR) throw new SVNException(SVNErrorMessage.create(SVNErrorCode.UNKNOWN, "Entry at URL ''{0}'' is a directory while a file was expected", filePath));
        boolean doesNotExist = nodeKind == SVNNodeKind.NONE;
        ISVNEditor editor;
        if (doesNotExist) {
            editor = repository.getCommitEditor(commitStr, null);
            editor.openRoot(-1);
            String[] dirs = dirPath.split("/");
            String curPath = "";
            for (int i = 0; i < dirs.length; i++) if (dirs[i] != null && dirs[i].length() > 0) {
                curPath += "/" + dirs[i];
                editor.openDir(curPath, -1);
            }
            logger.info("Creating file " + filePath);
            editor.addFile(filePath, null, -1);
        } else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            SVNProperties fileProperties = new SVNProperties();
            repository.getFile(filePath, -1, fileProperties, baos);
            String remoteChecksum = fileProperties.getStringValue("svn:entry:checksum");
            try {
                MessageDigest complete = MessageDigest.getInstance("MD5");
                complete.update(data, 0, data.length);
                byte[] checkSumBytes = complete.digest();
                String checkSum = "";
                for (int i = 0; i < checkSumBytes.length; i++) checkSum += Integer.toString((checkSumBytes[i] & 0xff) + 0x100, 16).substring(1);
                logger.fine("Remote MD5: " + remoteChecksum);
                logger.fine("New    MD5: " + checkSum);
                if (remoteChecksum.equals(checkSum)) {
                    logger.info("New text identical to remote SVN file - not committing " + filePath);
                    return;
                }
            } catch (NoSuchAlgorithmException e) {
            }
            editor = repository.getCommitEditor(commitStr, null);
            editor.openRoot(-1);
            logger.info("Updating file " + filePath);
            String[] dirs = dirPath.split("/");
            String curPath = "";
            for (int i = 0; i < dirs.length; i++) if (dirs[i] != null && dirs[i].length() > 0) {
                curPath += "/" + dirs[i];
                editor.openDir(curPath, -1);
            }
            editor.openFile(filePath, -1);
        }
        editor.applyTextDelta(filePath, null);
        SVNDeltaGenerator deltaGenerator = new SVNDeltaGenerator();
        String checksum = deltaGenerator.sendDelta(filePath, new ByteArrayInputStream(data), editor, true);
        editor.closeFile(filePath, checksum);
        editor.closeDir();
        editor.closeEdit();
    }
