            public void actionPerformed(ActionEvent e) {
                int result = fileChooser.showSaveDialog(ZooInspectorNodeViewersDialog.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    int answer = JOptionPane.YES_OPTION;
                    if (selectedFile.exists()) {
                        answer = JOptionPane.showConfirmDialog(ZooInspectorNodeViewersDialog.this, "The specified file already exists.  do you want to overwrite it?", "Confirm Overwrite", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    }
                    if (answer == JOptionPane.YES_OPTION) {
                        DefaultListModel listModel = (DefaultListModel) viewersList.getModel();
                        List<String> nodeViewersClassNames = new ArrayList<String>();
                        Object[] modelContents = listModel.toArray();
                        for (Object o : modelContents) {
                            nodeViewersClassNames.add(((ZooInspectorNodeViewer) o).getClass().getCanonicalName());
                        }
                        try {
                            manager.saveNodeViewersFile(selectedFile, nodeViewersClassNames);
                        } catch (IOException ex) {
                            LoggerFactory.getLogger().error("Error saving node veiwer configuration from file.", ex);
                            JOptionPane.showMessageDialog(ZooInspectorNodeViewersDialog.this, "Error saving node veiwer configuration from file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
