    private void check() {
        ValidationResult result = new ValidationResult();
        if (ValidationUtils.isBlank(nameField.getText())) result.addError("The name field is mandatory, please enter a unique name."); else {
            for (int i = 0; i < backupNames.length; i++) {
                if (backupNames[i].equalsIgnoreCase(nameField.getText())) {
                    result.addError("A backup already exists with that name. Please enter a different backup name.");
                    break;
                }
            }
        }
        if (ValidationUtils.isBlank(backupLocationField.getText())) result.addError("The backup location field is mandatory, please enter a valid directory path."); else if (!(new File(backupLocationField.getText())).exists()) result.addError("The backup location does not exist, please choose a different location."); else if (!(new File(backupLocationField.getText())).canRead()) result.addError("Can not write to the backup location, please choose another location that can be read from.");
        if (ValidationUtils.isBlank(saveLocationField.getText())) result.addError("The save location field is mandatory, please enter a valid directory to save backups too."); else if (!(new File(saveLocationField.getText())).exists()) result.addError("The save location does not exist, please choose a different location."); else if (!(new File(saveLocationField.getText())).canWrite()) result.addError("Can not write to the save location, please choose another location that can be written too."); else if ((new File(saveLocationField.getText() + File.separator + nameField.getText())).exists()) {
            result.addError("The backup location already has a directory named " + nameField.getText() + ". Please\nenter a different backup name or remove the directory.");
        }
        validationResult.setResult(result);
    }
