    private File getFilenameFromUser(JFileChooser fileChooser, String fileSuffix) {
        if (fileSuffix.indexOf('.') == -1) {
            fileSuffix = "." + fileSuffix;
        }
        File aFile = null;
        int userChoice = 0;
        String aFileName = null;
        outer: do {
            aFileName = null;
            userChoice = fileChooser.showSaveDialog(this);
            if (userChoice == JFileChooser.CANCEL_OPTION) {
                break;
            }
            File selectedFile = fileChooser.getSelectedFile();
            aFileName = selectedFile.getAbsolutePath();
            if (userChoice == JFileChooser.APPROVE_OPTION) {
                boolean notUniquelyNamed = true;
                while (notUniquelyNamed) {
                    if (aFileName.indexOf('.') == -1) {
                        aFileName = aFileName + "." + this.phylipFileSuffix;
                    }
                    aFile = new File(aFileName);
                    if (aFile.exists()) {
                        int returnVal = JOptionPane.showOptionDialog(this, "This file already exists.  " + "Do you wish to overwrite it?", "Overwrite File Confirmation", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
                        if (returnVal == JOptionPane.CANCEL_OPTION) {
                            return null;
                        }
                        if (returnVal == JOptionPane.NO_OPTION) {
                            aFileName = null;
                            continue outer;
                        }
                        if (returnVal == JOptionPane.YES_OPTION) {
                            notUniquelyNamed = false;
                        }
                    } else {
                        notUniquelyNamed = false;
                    }
                }
            }
        } while (aFileName == null);
        return aFile;
    }
