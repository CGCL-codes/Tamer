    private FileLock lockFile(File file) {
        try {
            FileOutputStream stream = new FileOutputStream(file);
            return stream.getChannel().lock();
        } catch (Exception e) {
            throw new RuntimeException("Unable to obtain file lock for file '" + file.getAbsolutePath() + "'.", e);
        }
    }
