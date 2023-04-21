    private void showSaveCharacterChooser(CharacterFacade character) {
        PCGenSettings context = PCGenSettings.getInstance();
        String parentPath = context.getProperty(PCGenSettings.PCG_SAVE_PATH);
        chooser.setCurrentDirectory(new File(parentPath));
        File file = character.getFileRef().getReference();
        File prevFile = file;
        if (file == null) {
            file = new File(parentPath, character.getNameRef().getReference() + Constants.s_PCGEN_CHARACTER_EXTENSION);
        }
        chooser.setSelectedFile(file);
        chooser.resetChoosableFileFilters();
        FileFilter filter = new PcgFileFilter();
        chooser.addChoosableFileFilter(filter);
        chooser.setFileFilter(filter);
        int ret = chooser.showSaveDialog(frame);
        if (ret == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
            if (!PCGFile.isPCGenCharacterFile(file)) {
                file = new File(file.getParent(), file.getName() + Constants.s_PCGEN_CHARACTER_EXTENSION);
            }
            UIDelegate delegate = character.getUIDelegate();
            if (file.isDirectory()) {
                delegate.showErrorMessage(Constants.s_APPNAME, "You cannot overwrite a directory with a character.");
                return;
            }
            if (file.exists() && (prevFile == null || !file.getName().equals(prevFile.getName()))) {
                boolean overwrite = delegate.showWarningConfirm("Confirm overwriting " + file.getName(), "The file " + file.getName() + " already exists, are you sure you want to overwrite it?");
                if (!overwrite) {
                    return;
                }
            }
            character.setFile(file);
            context.setProperty(PCGenSettings.PCG_SAVE_PATH, file.getParent());
            CharacterManager.saveCharacter(character);
        }
    }
