    private void copyFileToOverwrite() throws IOException {
        File fileNew = new File(file.getParent(), Source.normaliseName(file.getName()) + "-copied.tmp");
        FileUtils.copyFile(file, fileNew);
        session.put(Constants.SESSION_FILE, fileNew);
        session.put(Constants.SESSION_FILE_NAME, fileFileName);
        session.put(Constants.SESSION_FILE_CONTENT_TYPE, fileContentType);
    }
