            public void actionPerformed(ActionEvent e) {
                Object selection = resourceList.getSelectedValue();
                if (selection != null && selection instanceof ListItem) {
                    ListItem selectedItem = (ListItem) selection;
                    if (fileChooser.showSaveDialog(ResourceForkViewPanel.this) == JFileChooser.APPROVE_OPTION) {
                        File saveFile = fileChooser.getSelectedFile();
                        if (saveFile.exists()) {
                            int res = JOptionPane.showConfirmDialog(ResourceForkViewPanel.this, "The file already exists. Do you want to overwrite?", "Confirm overwrite", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                            if (res != JOptionPane.YES_OPTION) return;
                        }
                        ReadableRandomAccessStream in = null;
                        FileOutputStream fos = null;
                        try {
                            in = reader.getResourceStream(selectedItem.entry);
                            fos = new FileOutputStream(saveFile);
                            IOUtil.streamCopy(in, fos, 65536);
                        } catch (FileNotFoundException fnfe) {
                            JOptionPane.showMessageDialog(ResourceForkViewPanel.this, "Could not open file \"" + saveFile.getPath() + "\" for writing...", "Error", JOptionPane.ERROR_MESSAGE);
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                            GUIUtil.displayExceptionDialog(ioe, ResourceForkViewPanel.this);
                        } finally {
                            if (in != null) in.close();
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                    GUIUtil.displayExceptionDialog(ex, ResourceForkViewPanel.this);
                                }
                            }
                        }
                    }
                }
            }
