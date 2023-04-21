    static PrintStream getSVGFile() {
        File file;
        if (dir == null) dir = System.getProperty("user.dir");
        FileFilter fileFilter = new FileFilter() {

            public boolean accept(File aFile) {
                return (aFile.isDirectory() || aFile.getName().endsWith(".svg"));
            }

            public String getDescription() {
                return "SVG Files (*" + ".svg" + ")";
            }
        };
        while (true) {
            JFileChooser fileChooser = new JFileChooser(dir);
            fileChooser.addChoosableFileFilter(fileFilter);
            fileChooser.setFileFilter(fileFilter);
            int option = fileChooser.showSaveDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
                if (file.getName().endsWith(".svg")) {
                    dir = fileChooser.getCurrentDirectory().getAbsolutePath();
                    if (file.exists()) {
                        int i = JOptionPane.showConfirmDialog(null, "File already exists. Overwrite?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                        if (i != JOptionPane.YES_OPTION) continue;
                    }
                    return openFile(file.getAbsolutePath());
                } else {
                    JOptionPane.showMessageDialog(null, "The selected file must be of type .svg", "Invalid file type", JOptionPane.INFORMATION_MESSAGE);
                    continue;
                }
            } else {
                return null;
            }
        }
    }
