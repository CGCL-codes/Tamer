    public static final TempFileOutputStream viaTempFileOutputStream(String toPath, String tempPath) throws Exception {
        File to = new File(toPath);
        if (to.exists()) throw new Exception("Unable to write to file [" + toPath + "] already exists.");
        File temp = (tempPath == null ? to.getParentFile() : new File(tempPath));
        if (!temp.exists()) throw new Exception("The directory (" + temp.getAbsolutePath() + ") doesn't exists");
        if (!temp.isDirectory()) throw new Exception("The path (" + temp.getAbsolutePath() + ") doesn't point to a directory");
        File tempFile = null;
        try {
            tempFile = File.createTempFile("tmp.esis.", ".bin", temp);
            tempFile.deleteOnExit();
        } catch (IOException ioe) {
            _logger.error("Failed to create temp file with prefix (tmp.esis.) and suffix (.bin) in directory (" + temp.getAbsolutePath() + ")");
            throw ioe;
        }
        FileOutputStream fos = new FileOutputStream(tempFile);
        TempFileOutputStream tfos = new TempFileOutputStream();
        tfos.file = tempFile;
        tfos.fos = fos;
        return tfos;
    }
