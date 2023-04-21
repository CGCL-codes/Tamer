    public RobustFileOutputStream(File destFile, boolean createMissingParentDirectory) throws IOException {
        File parentDir = destFile.getParentFile();
        if (parentDir == null || !parentDir.isDirectory()) if ((createMissingParentDirectory && parentDir != null && parentDir.mkdirs()) == false) throw new IOException("Cannot write to file '" + destFile + "' - parent directory does not exist, " + "and could not be created.");
        String filename = destFile.getName();
        this.destFile = destFile;
        this.outFile = new File(parentDir, OUT_PREFIX + filename);
        this.backupFile = new File(parentDir, BACKUP_PREFIX + filename);
        if (destFile.isDirectory()) throw new IOException("Cannot write to file '" + destFile + "' - directory is in the way."); else if (destFile.exists() && !destFile.canWrite()) throw new IOException("Cannot write to file '" + destFile + "' - file is read-only.");
        origFileExists = destFile.isFile();
        checksum = makeChecksum();
        OutputStream outStream = new FileOutputStream(outFile);
        out = new CheckedOutputStream(outStream, checksum);
    }
