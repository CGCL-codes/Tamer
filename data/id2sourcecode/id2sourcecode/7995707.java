    protected void actionOpenHomeFolder(ActionEvent evt) {
        log.debug("actionOpenHomeFolder");
        if (Desktop.isDesktopSupported()) {
            File fHome = Util.getDefaultPath();
            try {
                FolderOpener opener = FolderOpenerFactory.createFolderOpener();
                if (opener != null) {
                    opener.openFolder(fHome);
                } else {
                    MessageBox.ok(this, resErrorMessages.getString("MainWindow.File.Folder.CantOpenFolderManager"));
                }
            } catch (IOException e) {
                log.error(e);
                MessageBox.okError(this, resErrorMessages.getString("MainWindow.File.Folder.CantOpenHomeFolder") + "\nErro:" + e.getMessage());
            } catch (Exception e) {
                log.error(e, e);
                MessageBox.okError(this, e.getMessage());
            }
        } else {
            log.warn("Can't open File Manager to open default Home Folder. Desktop is not supported.");
        }
    }
