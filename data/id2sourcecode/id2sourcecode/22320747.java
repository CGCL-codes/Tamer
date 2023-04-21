    public ResourceForkViewPanel(ResourceForkReader startupReader) {
        initComponents();
        resourceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        loadResourceFork(startupReader);
        resourceList.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                Object o = resourceList.getSelectedValue();
                if (o instanceof ListItem) setSelectedItem((ListItem) o); else if (o != null) JOptionPane.showMessageDialog(resourceList, "Unexpected type in list: " + o.getClass());
            }
        });
        viewButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Object selection = resourceList.getSelectedValue();
                if (selection != null && selection instanceof ListItem) {
                    ListItem selectedItem = (ListItem) selection;
                    JDialog d = new JDialog(JOptionPane.getFrameForComponent(ResourceForkViewPanel.this), selection.toString(), true);
                    DisplayTextFilePanel dtfp = new DisplayTextFilePanel();
                    dtfp.loadStream(reader.getResourceStream(selectedItem.entry));
                    d.add(dtfp);
                    d.pack();
                    d.setLocationRelativeTo(null);
                    d.setVisible(true);
                }
            }
        });
        extractButton.addActionListener(new ActionListener() {

            private JFileChooser fileChooser = new JFileChooser();

            {
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setMultiSelectionEnabled(false);
            }

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
        });
    }
