    public boolean savePC(PlayerCharacter aPC, boolean saveas) {
        boolean newPC = false;
        File prevFile;
        File file = null;
        String aPCFileName = aPC.getFileName();
        if (aPCFileName.equals("")) {
            prevFile = new File(SettingsHandler.getPcgPath().toString(), aPC.getDisplayName() + Constants.s_PCGEN_CHARACTER_EXTENSION);
            aPCFileName = prevFile.getAbsolutePath();
            newPC = true;
        } else {
            prevFile = new File(aPCFileName);
        }
        if (saveas || newPC) {
            JFileChooser fc = ImagePreview.decorateWithImagePreview(new JFileChooser());
            String[] pcgs = new String[] { "pcg" };
            SimpleFileFilter ff = new SimpleFileFilter(pcgs, "PCGen Character");
            fc.setFileFilter(ff);
            fc.setSelectedFile(prevFile);
            FilenameChangeListener listener = new FilenameChangeListener(aPCFileName, fc);
            fc.addPropertyChangeListener(listener);
            int returnVal = fc.showSaveDialog(GMGenSystem.inst);
            fc.removePropertyChangeListener(listener);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();
                if (!PCGFile.isPCGenCharacterFile(file)) {
                    file = new File(file.getParent(), file.getName() + Constants.s_PCGEN_CHARACTER_EXTENSION);
                }
                if (file.isDirectory()) {
                    JOptionPane.showMessageDialog(null, "You cannot overwrite a directory with a character.", Constants.s_APPNAME, JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if (file.exists() && (newPC || (prevFile == null) || !file.getName().equals(prevFile.getName()))) {
                    int reallyClose = JOptionPane.showConfirmDialog(GMGenSystem.inst, "The file " + file.getName() + " already exists, are you sure you want to overwrite it?", "Confirm overwriting " + file.getName(), JOptionPane.YES_NO_OPTION);
                    if (reallyClose != JOptionPane.YES_OPTION) {
                        return false;
                    }
                }
                aPC.setFileName(file.getAbsolutePath());
            } else {
                return false;
            }
        } else {
            file = prevFile;
        }
        try {
            (new PCGIOHandler()).write(aPC, file.getAbsolutePath());
            SettingsHandler.setPcgPath(file.getParentFile());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Could not save " + aPC.getDisplayName(), Constants.s_APPNAME, JOptionPane.ERROR_MESSAGE);
            Logging.errorPrint("Could not save " + aPC.getDisplayName());
            Logging.errorPrint(ex.getMessage(), ex);
            return false;
        }
        return true;
    }
