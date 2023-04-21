    public boolean saveAttachedFiles(final Bug newBug, final User userInAction, final File destDir, final List<File> newBugFile, final List<String> newBugFileFileName) {
        boolean wasError = false;
        BugFile bugFile;
        String randomName;
        File finalFile;
        File tempFile;
        for (int i = 0; i < newBugFile.size(); i++) {
            final File file = newBugFile.get(i);
            try {
                bugFile = new BugFile();
                bugFile.setBug(newBug);
                bugFile.setCreated(new Date());
                bugFile.setName(newBugFileFileName.get(i));
                randomName = UUID.randomUUID().toString();
                FileUtils.copyFileToDirectory(file, destDir);
                finalFile = new File(destDir, randomName);
                tempFile = new File(destDir, file.getName());
                tempFile.renameTo(finalFile);
                bugFile.setLocalFileName(randomName);
                bugFile.setUser(userInAction);
                this.bugFileDAO.persist(bugFile);
            } catch (final IOException e) {
                wasError = true;
            }
        }
        this.bugFileDAO.getHibernateTemplate().flush();
        return wasError;
    }
