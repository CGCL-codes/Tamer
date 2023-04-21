    private void jMenuItemNewActionPerformed(java.awt.event.ActionEvent evt) {
        if (!ed.currentFile.isEmpty()) {
            JOptionPane.showMessageDialog(ed.mainFrame, "First, close the running level.", "Attention", JOptionPane.WARNING_MESSAGE);
            return;
        }
        final JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Choose a file name");
        fc.setFileFilter(new FileFilter() {

            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(ed.LEVEL_FILE_EXTENSION) || f.isDirectory();
            }

            public String getDescription() {
                return "Level File (*" + ed.LEVEL_FILE_EXTENSION + ")";
            }
        });
        int ret = fc.showOpenDialog(ed.mainFrame);
        if (ret != JFileChooser.CANCEL_OPTION) {
            File file = fc.getSelectedFile();
            String filename = file.getAbsolutePath();
            if (!filename.endsWith(ed.LEVEL_FILE_EXTENSION)) {
                filename += ed.LEVEL_FILE_EXTENSION;
            }
            if (file.exists()) {
                if (JOptionPane.showConfirmDialog(ed.mainFrame, "File aready exists. Overwrite it?", "Attention", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
                    return;
                }
            }
            try {
                EntityBuilder eb = ed.getEntityBuilder();
                EntityGroup grp = new EntityGroup();
                eb.saveEntities(filename, grp);
                TextureAtlasContainer ac = new TextureAtlasContainer();
                ac.saveAtlases(filename + ed.ATLAS_FILE_EXTENSION);
            } catch (Exception e) {
                Log.error("cannot create file " + filename);
                return;
            }
            jButtonCam.setSelected(true);
            jButtonStats.setSelected(false);
            if (statePaneEntity != null) statePaneEntity.clear();
            if (statePaneTexture != null) statePaneTexture.clear();
            ed.currentFile = filename;
            Core.get().setAppState(statePaneEntity);
            statePaneEntity.initialize(ed.currentFile);
            statePaneTexture = jPaneTextures.getAppState();
            statePaneTexture.initialize();
            jPaneEntities.onLoad();
            jPaneTextures.onLoad();
            jTabbedPaneMain.setEnabled(true);
            canvas.setEnabled(true);
            ed.lastFile = new String(ed.currentFile);
            ed.saveEditorConfig();
            setTitle(EDITOR_TITLE + " - " + ed.currentFile);
            ed.undoManager.clear();
        }
    }
