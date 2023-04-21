    public void saveContent() {
        String fileToSave = chooseFile(this, SAVE);
        if (fileToSave != null) {
            if (!(new File(fileToSave)).exists()) save(fileToSave); else {
                int response = JOptionPane.showConfirmDialog(null, "File already exists, overwrite it ?", "Save", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                switch(response) {
                    case 0:
                        save(fileToSave);
                        break;
                    case 1:
                        break;
                    default:
                        return;
                }
            }
        }
    }
