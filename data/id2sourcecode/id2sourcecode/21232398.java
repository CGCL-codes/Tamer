            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileFilter(new FileNameExtensionFilter("Calculator Files", "dat"));
                if (chooser.showSaveDialog(GraphFrame.this) == JFileChooser.APPROVE_OPTION) {
                    String file = chooser.getSelectedFile().getPath();
                    if (file.lastIndexOf(".") < 0) file += ".dat";
                    if (file.substring(file.lastIndexOf(".") + 1).equals("dat")) {
                        try {
                            File f = new File(file);
                            if (!f.exists() || JOptionPane.showConfirmDialog(GraphFrame.this, "There is already a file with the name " + file.substring(file.lastIndexOf("\\") + 1) + ", are you sure you wish to overwrite it?") == JOptionPane.YES_OPTION) {
                                saveGraph(file);
                            }
                        } catch (HeadlessException e1) {
                        } catch (FileNotFoundException e1) {
                        } catch (IOException e1) {
                        }
                    }
                }
            }
