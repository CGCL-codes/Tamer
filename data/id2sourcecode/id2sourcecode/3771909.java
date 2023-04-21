            public void actionPerformed(ActionEvent arg0) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setDialogType(JFileChooser.OPEN_DIALOG);
                chooser.setSelectedFile(path);
                while (true) {
                    int open = chooser.showOpenDialog(null);
                    if (open == JFileChooser.APPROVE_OPTION) {
                        File fileToOpen = chooser.getSelectedFile();
                        if (fileToOpen.isDirectory() && fileToOpen.canRead() && fileToOpen.canWrite()) {
                            path = fileToOpen;
                            pc.put(ObjectKey.WRITE_DIRECTORY, path);
                            fileLabel.setText(path.getAbsolutePath());
                            break;
                        }
                        JOptionPane.showMessageDialog(null, "Selection must be a valid " + "(readable & writeable) Directory");
                        chooser.setSelectedFile(path);
                    } else if (open == JFileChooser.CANCEL_OPTION) {
                        break;
                    }
                }
            }
