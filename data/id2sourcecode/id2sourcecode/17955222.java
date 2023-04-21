    public void saveShow() {
        if (showLoaderSaver.getFileName().equals("")) {
            JFileChooser chooser = new JFileChooser();
            String filename = "";
            chooser.setFileFilter(new ShowFileFilter());
            chooser.setMultiSelectionEnabled(false);
            do {
                if (filename.equals("") == false) chooser.setSelectedFile(new File(filename));
                int returnVal = chooser.showSaveDialog(parent);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    filename = addFileExtension(chooser.getSelectedFile().getAbsolutePath());
                    if (chooser.getSelectedFile().exists()) {
                        int result = JOptionPane.showConfirmDialog(parent, filename + " already exists.  Do you want to replace this file?", "Overwrite File?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                        if (result == JOptionPane.YES_OPTION) {
                            showLoaderSaver.saveShow(filename);
                            break;
                        }
                    } else {
                        showLoaderSaver.saveShow(filename);
                        break;
                    }
                } else {
                    break;
                }
            } while (true);
        } else {
            showLoaderSaver.saveShow();
        }
    }
