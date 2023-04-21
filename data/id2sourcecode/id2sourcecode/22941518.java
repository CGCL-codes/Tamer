    public void showDialog(JComponent parent) {
        exportFrame = new JFrame();
        exportFrame.add(new JLabel("       Exporting..."));
        exportFrame.setSize(200, 80);
        exportFrame.setLocationRelativeTo(parent);
        exportFrame.setVisible(true);
        JFileChooser saveChooser = new JFileChooser();
        saveChooser.setDialogTitle("Export Tasks as CSV File");
        saveChooser.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                if (f.getName().endsWith(".csv")) {
                    return true;
                }
                return false;
            }

            @Override
            public String getDescription() {
                return "Comma Delimited Files (*.csv)";
            }
        });
        saveChooser.setSelectedFile(new File("export.csv"));
        if (saveChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            saveFile = saveChooser.getSelectedFile();
            if (saveFile.exists()) {
                int overwrite = JOptionPane.showConfirmDialog(parent, saveFile.getName() + " already exists, overwrite?", "CSV Export", JOptionPane.YES_NO_OPTION);
                if (overwrite == JOptionPane.NO_OPTION) {
                    return;
                }
            }
            approve = true;
        }
    }
