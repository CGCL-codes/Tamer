    public void startupMapHook() {
        super.startupMapHook();
        if (selectedCharset == null) {
            selectedCharset = PluginUtils.preselectCharset(getController(), "confluence.targetCharset", "8859_1");
        }
        String type = "txt";
        Container component = getController().getFrame().getContentPane();
        CharsetFileChooser chooser = null;
        chooser = new CharsetFileChooser(selectedCharset);
        File mmFile = getController().getMap().getFile();
        if (mmFile != null) {
            String proposedName = mmFile.getAbsolutePath().replaceFirst("\\.[^.]*?$", "") + "." + type;
            chooser.setSelectedFile(new File(proposedName));
        }
        if (getController().getLastCurrentDir() != null) {
            chooser.setCurrentDirectory(getController().getLastCurrentDir());
        }
        chooser.addChoosableFileFilter(new ImageFilter(type, null));
        int returnVal = chooser.showSaveDialog(component);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File chosenFile = chooser.getSelectedFile();
        getController().setLastCurrentDir(chosenFile.getParentFile());
        String ext = Tools.getExtension(chosenFile.getName());
        if (!Tools.safeEqualsIgnoreCase(ext, type)) {
            chosenFile = new File(chosenFile.getParent(), chosenFile.getName() + "." + type);
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
            exportToConfluenceMarkup(chosenFile, chooser.getSelectedCharset());
        } catch (IOException e) {
            freemind.main.Resources.getInstance().logException(e);
        }
        getController().getFrame().setWaitingCursor(false);
    }
