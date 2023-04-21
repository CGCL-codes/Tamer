    public static File saveAsDialog(Component parent) {
        String exportPath = Preference.getXmlExportDir();
        boolean retry = false;
        do {
            JFileChooser fc = (Utils.isBlank(exportPath) ? new JFileChooser() : new JFileChooser(exportPath));
            FileFilter filter = new XMLFileFilter();
            fc.setFileFilter(filter);
            int retval = fc.showSaveDialog(parent);
            if (retval == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fc.getSelectedFile();
                String fName = selectedFile.getName();
                int i = fName.lastIndexOf('.');
                if (i == -1) {
                    String path = selectedFile.getAbsolutePath() + ".xml";
                    selectedFile = new File(path);
                } else {
                    String extension = XMLFileFilter.getExtension(selectedFile);
                    if (extension == null || !extension.equals(XMLFileFilter.XML)) {
                        JOptionPane.showMessageDialog(parent, "File specified (" + fName + ") has a non .xml suffix", "File type error", JOptionPane.ERROR_MESSAGE);
                        retry = true;
                        continue;
                    }
                }
                if (selectedFile.exists()) {
                    int n = JOptionPane.showOptionDialog(parent, "File " + selectedFile.getName() + " already exists. Overwrite?", "XML Save", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                    if (n == JOptionPane.CANCEL_OPTION) {
                        return null;
                    } else if (n == JOptionPane.NO_OPTION) {
                        retry = true;
                    } else if (n == JOptionPane.YES_OPTION) {
                        return selectedFile;
                    }
                } else {
                    return selectedFile;
                }
            } else {
                return null;
            }
        } while (retry);
        return null;
    }
