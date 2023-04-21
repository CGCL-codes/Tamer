    private JButton getJButtonDialogButtonsExport() {
        if (jButtonDialogButtonsExport == null) {
            jButtonDialogButtonsExport = new JButton();
            jButtonDialogButtonsExport.setText("Export...");
            jButtonDialogButtonsExport.setToolTipText("Save and export the current settings to a file.");
            jButtonDialogButtonsExport.setMnemonic(KeyEvent.VK_E);
            jButtonDialogButtonsExport.setVisible(showImportExportButton);
            jButtonDialogButtonsExport.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    saveSettings();
                    JFrame dialog = new JFrame("JFileChooser Popup");
                    dialog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    Container contentPane = dialog.getContentPane();
                    final JFileChooser fc = new JFileChooser();
                    fc.setControlButtonsAreShown(true);
                    contentPane.add(fc, BorderLayout.CENTER);
                    File currentDir = new File(new File("").getAbsolutePath());
                    if (currentDir.exists()) {
                        fc.setCurrentDirectory(currentDir);
                    }
                    fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                    fc.setAcceptAllFileFilterUsed(false);
                    fc.setFileFilter(new FileFilter() {

                        public boolean accept(File file) {
                            if (file.isDirectory()) {
                                return true;
                            }
                            return (file.getName().endsWith(".xml"));
                        }

                        public String getDescription() {
                            return "XML Settings (*.xml)";
                        }
                    });
                    fc.setApproveButtonText("Export");
                    fc.setDialogTitle("Export tcpfile settings.");
                    int returnVal = fc.showOpenDialog(dialog);
                    if (returnVal == 0) {
                        File f = fc.getSelectedFile();
                        if (f != null) {
                            if (f.exists()) {
                                if (JOptionPane.showConfirmDialog(null, "Overwrite '" + f.getName() + "'?", "File already exists", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
                                    return;
                                }
                            }
                            try {
                                sm.saveToXML(f.getCanonicalPath());
                            } catch (IOException e1) {
                                log.error("", e1);
                            }
                        }
                    }
                }
            });
        }
        return jButtonDialogButtonsExport;
    }
