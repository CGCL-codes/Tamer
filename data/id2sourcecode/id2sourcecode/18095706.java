    private static File fileOk(String fileName, boolean read, boolean write) {
        if (fileName == null || fileName.trim().length() == 0) {
            LOG.warn("No hit counter file name");
            return null;
        }
        File hitFile = new File(fileName);
        if (read && !hitFile.exists()) {
            LOG.warn("No hit counter file: " + fileName);
            try {
                new FileWriter(hitFile);
                LOG.warn("Hit counter file created: " + fileName);
            } catch (IOException ex) {
                LOG.error("cannot create hit counter file: " + fileName);
            }
            return null;
        }
        if (read && !hitFile.canRead()) {
            LOG.error("Cannot read hit counter file: " + fileName);
            return null;
        }
        if (write && hitFile.exists() && !hitFile.canWrite()) {
            LOG.error("Cannot write hit counter file: " + fileName);
            return null;
        }
        return hitFile;
    }
