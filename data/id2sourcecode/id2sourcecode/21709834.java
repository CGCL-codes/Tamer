            public void actionPerformed(ActionEvent ae) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileFilter(new RDVFileFilter());
                int returnVal = chooser.showSaveDialog(frame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    if (file.getName().indexOf(".") == -1) {
                        file = new File(file.getAbsolutePath() + ".rdv");
                    }
                    if (file.exists()) {
                        int overwriteReturn = JOptionPane.showConfirmDialog(null, file.getName() + " already exists. Do you want to overwrite it?", "Overwrite file?", JOptionPane.YES_NO_OPTION);
                        if (overwriteReturn == JOptionPane.NO_OPTION) {
                            return;
                        }
                    }
                    ConfigurationManager.saveConfiguration(file);
                }
            }
