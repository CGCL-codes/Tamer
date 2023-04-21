    public void actionPerformed(ActionEvent e) {
        if (menu.activeProject) {
            int resSave = JOptionPane.showConfirmDialog(menu.getContentPane(), "Current Project NOT Saved ! Save it ?", "Save Project", JOptionPane.YES_NO_CANCEL_OPTION);
            if (resSave == 0) {
                if (menu.currentProjectFileName.equals("")) {
                    JFileChooser fc = new JFileChooser();
                    FoaFileFilter filter = new FoaFileFilter("xsl", "eXtensible Stylesheet Language Files");
                    fc.setFileFilter(filter);
                    fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
                    int action = fc.showSaveDialog(menu.getContentPane());
                    if (action == JFileChooser.APPROVE_OPTION) {
                        String fileName = fc.getSelectedFile().getAbsolutePath();
                        if (!fileName.endsWith(".xsl")) fileName += ".xsl";
                        if ((new File(fileName)).exists()) {
                            int resOver = JOptionPane.showConfirmDialog(menu.getContentPane(), "This file already exists ! Overwrite ?", "File Already Exists", JOptionPane.OK_CANCEL_OPTION);
                            if (resOver == 0) {
                                String fileSep = System.getProperty("file.separator");
                                writer.setAbsolutePath(fileName.substring(0, fileName.lastIndexOf(fileSep) + 1));
                                try {
                                    writer.writeXSLFile(fileName);
                                    JOptionPane.showMessageDialog(menu.getContentPane(), "Project File Saved !", "Message", JOptionPane.INFORMATION_MESSAGE);
                                } catch (Exception ex) {
                                    System.out.println("Writing error");
                                    ex.printStackTrace();
                                }
                            } else {
                                JOptionPane.showMessageDialog(menu.getContentPane(), "Project Saving Aborted !", "Message", JOptionPane.INFORMATION_MESSAGE);
                            }
                        } else {
                            String fileSep = System.getProperty("file.separator");
                            writer.setAbsolutePath(fileName.substring(0, fileName.lastIndexOf(fileSep) + 1));
                            try {
                                writer.writeXSLFile(fileName);
                                JOptionPane.showMessageDialog(menu.getContentPane(), "Project File Saved !", "Message", JOptionPane.INFORMATION_MESSAGE);
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(menu.getContentPane(), "Error: Project File NOT Saved !", "Error !", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                } else {
                    String fileSep = System.getProperty("file.separator");
                    writer.setAbsolutePath(menu.currentProjectFileName.substring(0, menu.currentProjectFileName.lastIndexOf(fileSep) + 1));
                    try {
                        writer.writeXSLFile(menu.currentProjectFileName);
                        JOptionPane.showMessageDialog(menu.getContentPane(), "Project File Saved !", "Message", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(menu.getContentPane(), "Error: Project File NOT Saved !", "Error !", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            System.exit(0);
        } else {
            System.exit(0);
        }
    }
