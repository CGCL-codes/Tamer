    public void approveSelection() {
        File file = getSelectedFile();
        if (file == null) {
        } else {
            if (getDialogType() == JFileChooser.SAVE_DIALOG) {
                String fileName = file.getAbsolutePath();
                File newfile = file;
                if (fileName != null && !sRequiredExtension.equals("")) {
                    if (!fileName.toLowerCase().endsWith(sRequiredExtension)) {
                        fileName = fileName + sRequiredExtension;
                        newfile = new File(fileName);
                        setSelectedFile(newfile);
                    }
                }
                if (newfile.exists()) {
                    int answer = JOptionPane.showConfirmDialog(this, "This file exists already. Over write it?", "Warning", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (answer == JOptionPane.OK_OPTION) {
                        super.approveSelection();
                    } else {
                    }
                } else {
                    super.approveSelection();
                }
            } else if (getDialogType() == JFileChooser.OPEN_DIALOG) {
                String fileName = file.getAbsolutePath();
                if (fileName != null) {
                    if (!sRequiredExtension.equals("")) {
                        if (!fileName.toLowerCase().endsWith(sRequiredExtension)) {
                            JOptionPane.showMessageDialog(this, "You must select a file of type '" + sRequiredExtension + "'");
                        } else {
                            super.approveSelection();
                        }
                    } else {
                        super.approveSelection();
                    }
                }
            } else {
                super.approveSelection();
            }
        }
    }
