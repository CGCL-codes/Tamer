    public static final File successTempFileOutputStream(TempFileOutputStream tfos, String toPath) throws Exception {
        tfos.fos.close();
        tfos.fos = null;
        File newFile = new File(toPath);
        if (newFile.exists()) {
            throw new Exception("Unable to write to file [" + toPath + "] already exists.");
        }
        if (!tfos.file.renameTo(newFile)) {
            throw new Exception("Unable to rename " + tfos.file.getAbsolutePath() + " to " + toPath);
        }
        return newFile;
    }
