            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setFileFilter(new FileFilter() {

                    @Override
                    public boolean accept(File f) {
                        return true;
                    }

                    @Override
                    public String getDescription() {
                        return "All Files";
                    }
                });
                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                boolean done = false;
                while (!done) {
                    fc.setSelectedFile(new File(fc.getCurrentDirectory().getAbsolutePath() + File.separator + controller.getClassName() + "Test.java"));
                    int returnVal = fc.showSaveDialog(thisItem);
                    File selectedFile = fc.getSelectedFile();
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        if (selectedFile.exists()) {
                            int result = JOptionPane.showConfirmDialog(thisItem, "File Already Exists!\n Would you like to overwrite it?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                            if (JOptionPane.YES_OPTION == result) {
                                FileSaver fileSaver = new FileSaver(testStr, selectedFile);
                                done = true;
                                JOptionPane.showMessageDialog(thisItem, "File Saved!", "File Writer", JOptionPane.INFORMATION_MESSAGE);
                            }
                        } else {
                            FileSaver fileSaver = new FileSaver(testStr, selectedFile);
                            done = true;
                            JOptionPane.showMessageDialog(thisItem, "File Saved!", "File Writer", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else {
                        done = true;
                    }
                }
            }
