    public void zipContent() {
        if (getText().equals("")) return;
        String zipFile = chooseFile(this, SAVE);
        if (zipFile != null) {
            if (!zipFile.endsWith(".zip")) zipFile += ".zip";
            if (!(new File(zipFile)).exists()) zip(zipFile); else {
                int response = JOptionPane.showConfirmDialog(null, "File already exists, overwrite it ?", "Save", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                switch(response) {
                    case 0:
                        zip(zipFile);
                        break;
                    case 1:
                        break;
                    default:
                        return;
                }
            }
        }
    }
