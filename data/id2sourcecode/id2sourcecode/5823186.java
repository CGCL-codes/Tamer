    public void saveChoose() {
        JFileChooser chooser = null;
        boolean overwrite = false;
        File savedFile;
        while (!overwrite) {
            chooser = new JFileChooser();
            int option = chooser.showSaveDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                savedFile = chooser.getSelectedFile();
            } else {
                return;
            }
            if (savedFile.exists()) {
                int n = JOptionPane.showConfirmDialog(chooser, "Warning - A file already exists by this name!\n" + "Do you want to overwrite it?\n", "Save Confirmation", JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION) {
                    overwrite = true;
                } else if (n == JOptionPane.CANCEL_OPTION) {
                    chooser.cancelSelection();
                    getRootScape().getRunner().resume();
                }
            } else {
                overwrite = true;
            }
        }
        if (chooser.getSelectedFile() != null) {
            try {
                getRootScape().save(chooser.getSelectedFile());
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Sorry, couldn't save model because an input/output exception occured:\n" + e, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "You must enter a file name or cancel.", "Message", JOptionPane.INFORMATION_MESSAGE);
            saveChoose();
        }
    }
