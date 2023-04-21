    public static File createOrRetrieveDir(String dirPath) throws IOException {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            boolean created = false;
            try {
                created = dir.mkdirs();
            } catch (Exception e) {
                throw new IOException("Error reading/writing to directory '" + dir.getAbsolutePath() + ". Cause: " + e.getMessage());
            }
            if (!created) {
                throw new IOException("Unable to read and write to directory '" + dir.getAbsolutePath());
            }
        }
        return dir;
    }
