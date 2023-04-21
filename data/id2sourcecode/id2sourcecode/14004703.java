    public void actionPerformed(ActionEvent e) {
        if (menu.activeProject) {
            int resSave = JOptionPane.showConfirmDialog(container, "Current Project NOT Saved ! Save it ?", "Save Project", JOptionPane.YES_NO_CANCEL_OPTION);
            if (resSave == 0) {
                if (menu.currentProjectFileName.equals("")) {
                    JFileChooser fc = new JFileChooser();
                    FoaFileFilter filter = new FoaFileFilter("xsl", "eXtensible Stylesheet Language Files");
                    fc.setFileFilter(filter);
                    fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
                    int action = fc.showSaveDialog(container);
                    if (action == JFileChooser.APPROVE_OPTION) {
                        fileName = fc.getSelectedFile().getAbsolutePath();
                        if (!fileName.endsWith(".xsl")) fileName += ".xsl";
                        if ((new File(fileName)).exists()) {
                            int resOver = JOptionPane.showConfirmDialog(container, "This file already exists ! Overwrite ?", "File Already Exists", JOptionPane.OK_CANCEL_OPTION);
                            if (resOver == 0) {
                                String fileSep = System.getProperty("file.separator");
                                writer.setAbsolutePath(fileName.substring(0, fileName.lastIndexOf(fileSep) + 1));
                                try {
                                    writer.writeXSLFile(fileName);
                                    JOptionPane.showMessageDialog(container, "Project File Saved !", "Message", JOptionPane.INFORMATION_MESSAGE);
                                } catch (Exception ex) {
                                    JOptionPane.showMessageDialog(container, "Error: Project File NOT Saved !", "Error !", JOptionPane.ERROR_MESSAGE);
                                    ex.printStackTrace();
                                }
                            } else {
                                JOptionPane.showMessageDialog(container, "Project Saving Aborted !", "Message", JOptionPane.INFORMATION_MESSAGE);
                            }
                        } else {
                            String fileSep = System.getProperty("file.separator");
                            writer.setAbsolutePath(fileName.substring(0, fileName.lastIndexOf(fileSep) + 1));
                            try {
                                writer.writeXSLFile(fileName);
                                JOptionPane.showMessageDialog(container, "Project File Saved !", "Message", JOptionPane.INFORMATION_MESSAGE);
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(container, "Error: Project File NOT Saved !", "Error !", JOptionPane.ERROR_MESSAGE);
                                ex.printStackTrace();
                            }
                        }
                    }
                } else {
                    String fileSep = System.getProperty("file.separator");
                    writer.setAbsolutePath(fileName.substring(0, fileName.lastIndexOf(fileSep) + 1));
                    try {
                        writer.writeXSLFile(fileName);
                        JOptionPane.showMessageDialog(container, "Project File Saved !", "Message", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(container, "Error: Project File NOT Saved !", "Error !", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            menu.activeProject = false;
            menu.getFlowDirector().getContentManager().resetContent();
            menu.getFlowDirector().getContentSequenceManager().resetContentSequences();
            menu.getAttributeDirector().resetAttributes();
            menu.getLayoutDirector().getPageManager().initializePages();
            menu.getLayoutDirector().getPageSequenceManager().initializePageSequences();
            menu.getBrickDirector().getBrickManager().initializeBricks();
            JFileChooser fc = new JFileChooser();
            FoaFileFilter filter = new FoaFileFilter("xsl", "eXtensible Stylesheet Language Files");
            fc.setFileFilter(filter);
            if (!menu.currentProjectFileName.equals("")) {
                fc.setCurrentDirectory(new File(menu.currentProjectFileName));
            } else {
                fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
            }
            menu.currentProjectFileName = "";
            int action = fc.showOpenDialog(container);
            if (action == JFileChooser.APPROVE_OPTION) {
                fileName = fc.getSelectedFile().getAbsolutePath();
                if (fileName.endsWith("Atts.xsl")) {
                    JOptionPane.showMessageDialog(container, "This XSL file is an Attribute Set File !!!", "Operation Aborted !", JOptionPane.ERROR_MESSAGE);
                } else {
                    String fileSep = System.getProperty("file.separator");
                    menu.currentProjectPath = fileName.substring(0, fileName.lastIndexOf(fileSep) + 1);
                    reader.setAbsolutePath(menu.currentProjectPath);
                    loading = new JDialog();
                    JPanel buttonPanel = new JPanel();
                    JButton okButton = new JButton(new CancelAction(loading));
                    okButton.setLabel("Ok");
                    buttonPanel.add(okButton);
                    messageArea = new JTextArea();
                    messageArea.setEditable(false);
                    JScrollPane scrollPane = new JScrollPane(messageArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                    panel = new JPanel(new BorderLayout());
                    panel.add(new JLabel("Loading Project file ..."), BorderLayout.NORTH);
                    panel.add(scrollPane, BorderLayout.CENTER);
                    panel.add(buttonPanel, BorderLayout.SOUTH);
                    loading.setTitle("Loading");
                    loading.getContentPane().add(panel);
                    loading.setSize(400, 250);
                    loading.show();
                    menu.activeProject = true;
                    menu.setCurrentProjectFileName(fileName);
                    reader.parseXmlFileWithSAX(fileName, menu.getContentPane(), messageArea, menu);
                    loading.setModal(true);
                    if (menu.activeProject) activateItemsButtons();
                }
            }
        } else {
            JFileChooser fc = new JFileChooser();
            FoaFileFilter filter = new FoaFileFilter("xsl", "eXtensible Stylesheet Language Files");
            fc.setFileFilter(filter);
            fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
            int action = fc.showOpenDialog(container);
            if (action == JFileChooser.APPROVE_OPTION) {
                fileName = fc.getSelectedFile().getAbsolutePath();
                if (fileName.endsWith("Atts.xsl")) {
                    JOptionPane.showMessageDialog(container, "This XSL file is an Attribute Set File !!!", "Operation Aborted !", JOptionPane.ERROR_MESSAGE);
                } else {
                    String fileSep = System.getProperty("file.separator");
                    menu.currentProjectPath = fileName.substring(0, fileName.lastIndexOf(fileSep) + 1);
                    reader.setAbsolutePath(menu.currentProjectPath);
                    loading = new JDialog();
                    JPanel buttonPanel = new JPanel();
                    JButton okButton = new JButton(new CancelAction(loading));
                    okButton.setLabel("Ok");
                    buttonPanel.add(okButton);
                    messageArea = new JTextArea();
                    messageArea.setEditable(false);
                    JScrollPane scrollPane = new JScrollPane(messageArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                    panel = new JPanel(new BorderLayout());
                    panel.add(new JLabel("Loading Project file ..."), BorderLayout.NORTH);
                    panel.add(scrollPane, BorderLayout.CENTER);
                    panel.add(buttonPanel, BorderLayout.SOUTH);
                    loading.setTitle("Loading");
                    loading.getContentPane().add(panel);
                    loading.setSize(400, 250);
                    loading.show();
                    menu.activeProject = true;
                    menu.setCurrentProjectFileName(fileName);
                    reader.parseXmlFileWithSAX(fileName, menu.getContentPane(), messageArea, menu);
                    loading.setModal(true);
                    if (menu.activeProject) activateItemsButtons();
                }
            }
        }
    }
