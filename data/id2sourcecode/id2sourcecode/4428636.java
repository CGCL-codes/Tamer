    public void publish(final Element cruisecontrolLog) throws CruiseControlException {
        final XMLLogHelper helper = new XMLLogHelper(cruisecontrolLog);
        if (helper.isBuildSuccessful()) {
            final Project project = new Project();
            if (strSource == null) {
                strSource = project.getBaseDir().getPath() + File.separator + "dist";
            }
            final File dirTarget = new File(this.strTarget);
            if (!dirTarget.exists()) {
                throw new CruiseControlException("target directory " + dirTarget.getAbsolutePath() + " does not exist");
            }
            if (!dirTarget.isDirectory()) {
                throw new CruiseControlException("target directory " + dirTarget.getAbsolutePath() + " is not a directory");
            }
            final File dirSource = new File(this.strSource);
            if (!dirSource.exists()) {
                throw new CruiseControlException("source directory " + dirSource.getAbsolutePath() + " does not exist");
            }
            if (!dirSource.isDirectory()) {
                throw new CruiseControlException("source directory " + dirSource.getAbsolutePath() + " is not a directory");
            }
            final File fileJnlp = new File(this.strJnlp);
            if (!fileJnlp.exists()) {
                throw new CruiseControlException("jnlp file " + fileJnlp.getAbsolutePath() + " does not exist");
            }
            String strEntry = null;
            String strNewFile = null;
            String strVersion = null;
            for (final File file : dirSource.listFiles()) {
                final String strFilename = file.getName();
                final int idxVersion = strFilename.indexOf("__V");
                final int idxJar = strFilename.lastIndexOf(".jar");
                if (idxVersion > 0 && idxVersion < idxJar) {
                    final String strTmp = strFilename.substring(idxVersion + 3, idxJar);
                    if (strVersion == null || strVersion.compareTo(strTmp) > 0) {
                        strEntry = strFilename.substring(0, idxVersion);
                        strNewFile = strFilename;
                        strVersion = strTmp;
                    }
                }
            }
            if (strNewFile != null && strVersion != null) {
                final FileUtils utils = FileUtils.getFileUtils();
                try {
                    if (this.bDeleteOldJars) {
                        for (final File file : dirTarget.listFiles()) {
                            if (file.getName().indexOf(strEntry + "__V") > -1) {
                                if (!file.delete()) {
                                    LOG.warn("Failed to delete old jnlp jar file: " + file.getAbsolutePath());
                                }
                            }
                        }
                    }
                    utils.copyFile(new File(dirSource, strNewFile), new File(dirTarget, strNewFile));
                } catch (IOException e) {
                    throw new CruiseControlException(e);
                }
                try {
                    updateJnlpFile(this.strJnlp, strEntry, strVersion);
                } catch (Exception ex) {
                    throw new CruiseControlException(ex);
                }
            }
        }
    }
