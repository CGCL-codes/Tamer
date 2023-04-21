    public void switchFiles() throws LogException {
        ++currentFile;
        String fname = getFileName(currentFile);
        File file = new File(dir, fname);
        if (file.exists()) {
            if (LOG.isDebugEnabled()) LOG.debug("Journal file " + file.getAbsolutePath() + " already exists. Copying it.");
            boolean renamed = file.renameTo(new File(file.getAbsolutePath() + BAK_FILE_SUFFIX));
            if (renamed && LOG.isDebugEnabled()) LOG.debug("Old file renamed to " + file.getAbsolutePath());
            file = new File(dir, fname);
        }
        if (LOG.isDebugEnabled()) LOG.debug("Creating new journal: " + file.getAbsolutePath());
        synchronized (latch) {
            close();
            try {
                FileOutputStream os = new FileOutputStream(file, true);
                channel = os.getChannel();
                syncThread.setChannel(channel);
            } catch (FileNotFoundException e) {
                throw new LogException("Failed to open new journal: " + file.getAbsolutePath(), e);
            }
        }
        inFilePos = 0;
    }
