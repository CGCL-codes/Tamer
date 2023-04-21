    public void performUpdate2(List<File> files, IStatusHandler handler) {
        progressBar.setIndeterminate(false);
        progressBar.setMinimum(0);
        progressBar.setIndeterminate(true);
        lblNewLabel.setText("Installing files... Do not close the window.");
        try {
            int progress = 1;
            if (prefs.isCreateBackupsEnabled()) {
                if (prefs.isCreateFullBackupsEnabled()) {
                    setProgress(progress++, "Creating full backup...");
                    Application.getInstance().getBackupManager().fullBackup();
                } else {
                    setProgress(progress++, "Creating backup of minecraft.jar...");
                    Application.getInstance().getBackupManager().backupJarOnly();
                }
            }
            setProgress(progress++, "Preparing mods...");
            File dir = prefs.getMinecraftDirAsFile();
            if (!prefs.isValidMinecraftDir(dir)) {
                dir = Application.getInstance().chooseMinecraftDir();
                if (dir == null) {
                    close();
                    return;
                }
                prefs.setMinecraftDir(dir.getAbsolutePath());
            }
            if (!prefs.isValidMinecraftDir(dir)) {
                throw new Exception("You must choose a valid minecraft installation directory");
            }
            setMessage("Extracting minecraft.jar...");
            log.info("extracting...");
            File tmpArea = prefs.getTmpArea();
            File mineDir = File.createTempFile("minecraft-", "-jar", tmpArea);
            if (mineDir.exists()) mineDir.delete();
            mineDir.mkdirs();
            if (!mineDir.exists() || !mineDir.isDirectory()) {
                throw new Exception("Can't create directory: " + mineDir);
            }
            Util.unzip(prefs.getMinecraftJar(), mineDir);
            File meta = new File(mineDir, "META-INF");
            if (meta.exists()) {
                setMessage("deleting META-INF");
                FileUtils.deleteDirectory(meta);
            }
            for (File f : files) {
                try {
                    if (!f.exists()) {
                        throw new Exception("File Not Found: " + f);
                    }
                    setMessage("");
                    setProgress(progress++, "Extracting " + f.getName());
                    updateMinecraft(f, mineDir);
                    handler.installed(f);
                } catch (Throwable t) {
                    handler.fail(f, t);
                }
            }
            setProgress(progress++, "Creating new minecraft.jar");
            String name = BackupManager.createBackupName("minecraft.jar");
            File newjar = new File(prefs.getTmpArea(), name);
            Util.zip(mineDir, newjar);
            setProgress(progress++, "Installing new minecraft.jar");
            FileUtils.copyFile(newjar, prefs.getMinecraftJar());
            File mockMineDir = new File(prefs.getTmpArea(), "mockminecraft");
            if (mockMineDir.exists()) {
                setProgress(progress++, "Copying Resources...");
                FileUtils.copyDirectory(mockMineDir, prefs.getMinecraftDirAsFile(), new TrackingFileFilter(mockMineDir, true), true);
            }
            setProgress(MAX_TASKS, "<html><b><font color='green'>Done</font></b></html>");
            lblNewLabel.setText("<html><b><font color='green'>Installing complete.  You can now close this window.</font></b></html>");
        } catch (Exception e) {
            lblNewLabel.setText("<html><b><font color=red>Installation Failed.  You should restore your Minecraft.jar</font></b></html>");
            log.error("Failed to install mods", e);
            Application.getInstance().error("Error", "Install Failed: " + e.getMessage() + "; See log for details.");
        } finally {
            btnOk.setText("Close");
            try {
                FileUtils.deleteDirectory(prefs.getTmpArea());
            } catch (IOException e) {
                setMessage("<html><font color='red'>Unable to clean out tmp area: " + prefs.getTmpArea() + "; You should manually delete this location.</font></html>");
                log.warn("Could not clean up the tmp area: " + prefs.getTmpArea(), e);
            }
            progressBar.setValue(0);
            progressBar.setIndeterminate(false);
        }
    }
