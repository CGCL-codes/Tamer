    private void chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose a file name");
        int chooserStatus = fileChooser.showDialog(this, "Select target file");
        if (chooserStatus == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            switch(fileStatus(selectedFile)) {
                case WRITABLE_FILE:
                    if (selectedFile.exists()) {
                        Object[] overwriteOptions = { "Overwrite", "Choose another file", "Don't choose a file" };
                        int overwrite = JOptionPane.showOptionDialog(this, "The file you've chosen already exists.\n" + "Do you want to overwrite this file?", "Overwrite file?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, overwriteOptions, overwriteOptions[1]);
                        if (overwrite == 0) {
                            setOutputFile(selectedFile);
                        } else if (overwrite == 1) {
                            chooseFile();
                        }
                    } else {
                        setOutputFile(selectedFile);
                    }
                    break;
                case INVALID_FILE:
                    chooseFile();
            }
        }
    }
