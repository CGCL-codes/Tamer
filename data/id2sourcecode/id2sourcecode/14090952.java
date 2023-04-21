    private void pushButtonArchive() {
        if (Global.isThreadActive(ThreadCompile.class)) {
            return;
        }
        this.updateAllComponents(false);
        final String lcFileName = this.fcNewArchiveFileName.toString();
        final File loFile = new File(lcFileName);
        if (loFile.exists()) {
            if (!Util.yesNo(this, lcFileName + " already exists. Do you want to overwrite it?")) {
                return;
            }
            if (!loFile.delete()) {
                Util.errorMessage(this, "Unable to delete the file of " + loFile.getPath() + ". It is probably open in another program.");
                return;
            }
        }
        final String[] laFoldersToArchive = this.foAppProperties.getGridFoldersToArchive();
        if ((laFoldersToArchive == null) || (laFoldersToArchive.length == 0)) {
            Util.errorMessage(this, "You have not yet selected any folders to archive.");
            return;
        }
        Object loCallbackObject = null;
        Method lmCallbackMethod = null;
        Object[] laParameters = null;
        try {
            loCallbackObject = this;
            lmCallbackMethod = this.getClass().getMethod("updateAllComponents", new Class[] { Boolean.class });
            laParameters = new Object[] { Boolean.valueOf(true) };
        } catch (final Exception loErr) {
            loCallbackObject = null;
            lmCallbackMethod = null;
            laParameters = null;
            Global.errorException(this, "There were problems in setting the callback method:<br>" + loErr.getMessage());
        }
        final ThreadCompile loThread = new ThreadCompile(this, this, loFile, this.foAppProperties.getOptionIncludeHiddenDirectoriesBZ(), this.foAppProperties.getOptionIncludeHiddenFilesBZ(), this.foAppProperties.getOptionCompressionLevelBZ(), laFoldersToArchive, ZipComment.buildBeoZipComment(laFoldersToArchive), loCallbackObject, lmCallbackMethod, laParameters);
        loThread.start();
    }
