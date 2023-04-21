    public boolean saveBallotButtonPressed() {
        try {
            JFileChooser fc = fileChooser;
            fc.setFileFilter(new FileFilter() {

                @Override
                public boolean accept(File f) {
                    String path = f.getAbsolutePath();
                    return (f.isDirectory() || path.length() > 4 && path.substring(path.length() - 4).equals(".bal"));
                }

                @Override
                public String getDescription() {
                    return "Ballot files";
                }
            });
            int answer = fc.showSaveDialog(this);
            if (answer == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                if (file.exists()) {
                    answer = JOptionPane.showConfirmDialog(this, "The file you selected already exists. Overwrite?", "Overwrite Saved Ballot", JOptionPane.YES_NO_OPTION);
                    if (answer == JOptionPane.YES_OPTION) {
                        model.saveAs(file.getAbsolutePath());
                        return true;
                    }
                } else {
                    model.saveAs(file.getAbsolutePath());
                    return true;
                }
            }
        } catch (BallotSaveException e) {
            JOptionPane.showMessageDialog(this, "An error occurred while saving the ballot.\nPlease verify the directory is writable, and try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
