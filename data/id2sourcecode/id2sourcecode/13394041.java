    @Override
    public void startupMapHook() {
        super.startupMapHook();
        initConfluenceFileChooser();
        Container component = getController().getFrame().getContentPane();
        File mmFile = getController().getMap().getFile();
        if (mmFile != null) {
            String proposedName = mmFile.getAbsolutePath().replaceFirst("\\.[^.]*?$", "") + "." + CONFLUENCE_FILE_EXTENSION;
            confluenceFileChooser.setSelectedFile(new File(proposedName));
        }
        if (getController().getLastCurrentDir() != null) {
            confluenceFileChooser.setCurrentDirectory(getController().getLastCurrentDir());
        }
        if (confluenceFileChooser.showSaveDialog(component) == JFileChooser.APPROVE_OPTION) {
            File chosenFile = confluenceFileChooser.getSelectedFile();
            getController().setLastCurrentDir(chosenFile.getParentFile());
            String ext = Tools.getExtension(chosenFile.getName());
            if (!Tools.safeEqualsIgnoreCase(ext, CONFLUENCE_FILE_EXTENSION)) {
                chosenFile = new File(chosenFile.getParent(), chosenFile.getName() + "." + CONFLUENCE_FILE_EXTENSION);
            }
            if (chosenFile.exists()) {
                String overwriteText = MessageFormat.format(getController().getText("file_already_exists"), new Object[] { chosenFile.toString() });
                int overwriteMap = JOptionPane.showConfirmDialog(component, overwriteText, overwriteText, JOptionPane.YES_NO_OPTION);
                if (overwriteMap != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            getController().getFrame().setWaitingCursor(true);
            try {
                exportToFile(chosenFile, confluenceFileChooser.getSelectedCharset());
            } catch (IOException e) {
                freemind.main.Resources.getInstance().logException(e, "Failed to export mind-map to Confluence markup.");
                JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
            }
            getController().getFrame().setWaitingCursor(false);
        }
    }
