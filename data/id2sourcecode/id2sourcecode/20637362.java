    protected File chooseFile(String type, String description) {
        Container component = getController().getFrame().getContentPane();
        JFileChooser chooser = null;
        chooser = new JFileChooser();
        File mmFile = getController().getMap().getFile();
        if (mmFile != null) {
            String proposedName = mmFile.getAbsolutePath().replaceFirst("\\.[^.]*?$", "") + "." + type;
            chooser.setSelectedFile(new File(proposedName));
        }
        chooser.addChoosableFileFilter(new ImageFilter(type, description));
        int returnVal = chooser.showSaveDialog(component);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        File chosenFile = chooser.getSelectedFile();
        String ext = Tools.getExtension(chosenFile.getName());
        if (!Tools.safeEqualsIgnoreCase(ext, type)) {
            chosenFile = new File(chosenFile.getParent(), chosenFile.getName() + "." + type);
        }
        if (chosenFile.exists()) {
            String overwriteText = MessageFormat.format(getController().getText("file_already_exists"), new Object[] { chosenFile.toString() });
            int overwriteMap = JOptionPane.showConfirmDialog(component, overwriteText, overwriteText, JOptionPane.YES_NO_OPTION);
            if (overwriteMap != JOptionPane.YES_OPTION) {
                return null;
            }
        }
        return chosenFile;
    }
