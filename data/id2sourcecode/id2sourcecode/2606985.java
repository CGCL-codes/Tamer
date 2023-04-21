    public void file_save() {
        loadProperties();
        String P_BROWSE_LOCATION = "browse.location";
        if (!c.getlayer().isEmpty()) {
            JFileChooser fileChooser = new JFileChooser() {

                private static final long serialVersionUID = 1L;

                @Override
                public void approveSelection() {
                    File f = getSelectedFile();
                    if (!f.getName().endsWith("map")) {
                        f = new File(f.toString() + ".map");
                    }
                    if (f.exists() && getDialogType() == SAVE_DIALOG) {
                        int result = JOptionPane.showConfirmDialog(getTopLevelAncestor(), "The selected file already exists. " + "Do you want to overwrite it?", "The file already exists", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                        switch(result) {
                            case JOptionPane.YES_OPTION:
                                super.approveSelection();
                                return;
                            case JOptionPane.NO_OPTION:
                                return;
                            case JOptionPane.CANCEL_OPTION:
                                cancelSelection();
                                return;
                        }
                    }
                    super.approveSelection();
                }
            };
            fileChooser.setFileFilter(new FileFilter() {

                public boolean accept(File file) {
                    return file.getName().toLowerCase().endsWith(".map") || file.isDirectory();
                }

                public String getDescription() {
                    return "Map (*.map)";
                }
            });
            if (properties.containsKey(P_BROWSE_LOCATION)) {
                fileChooser.setCurrentDirectory(new File(properties.getProperty(P_BROWSE_LOCATION)));
            }
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                String filename = fileChooser.getSelectedFile().getName();
                String path = fileChooser.getCurrentDirectory().toString();
                write_map(path, filename);
            }
        }
    }
