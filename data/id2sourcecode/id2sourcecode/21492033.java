    public void actionPerformed(ActionEvent e) {
        if (menu.getCurrentProjectFileName().equals("")) {
            JFileChooser fc = new JFileChooser();
            FoaFileFilter filter = new FoaFileFilter("xsl", "eXtensible Stylesheet Language Files");
            fc.setFileFilter(filter);
            fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
            int action = fc.showSaveDialog(container);
            if (action == JFileChooser.APPROVE_OPTION) {
                fileName = fc.getSelectedFile().getAbsolutePath();
                if (!fileName.endsWith(".xsl")) fileName += ".xsl";
                if ((new File(fileName)).exists()) {
                    int result = JOptionPane.showConfirmDialog(container, "This file already exists ! Overwrite ?", "File Already Exists", JOptionPane.YES_NO_CANCEL_OPTION);
                    if (result == 0) {
                        menu.setCurrentProjectFileName(fileName);
                        String fileSep = System.getProperty("file.separator");
                        writer.setAbsolutePath(fileName.substring(0, fileName.lastIndexOf(fileSep) + 1));
                        try {
                            writer.writeXSLFile(fileName);
                            JOptionPane.showMessageDialog(container, "Project File Saved !", "Message", JOptionPane.INFORMATION_MESSAGE);
                        } catch (Exception ex) {
                            System.out.println("Writing error");
                            ex.printStackTrace();
                        }
                    } else {
                        JOptionPane.showMessageDialog(container, "Project Saving Aborted !", "Message", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    menu.setCurrentProjectFileName(fileName);
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
            writer.setAbsolutePath(menu.getCurrentProjectFileName().substring(0, menu.getCurrentProjectFileName().lastIndexOf(fileSep) + 1));
            try {
                writer.writeXSLFile(menu.getCurrentProjectFileName());
                JOptionPane.showMessageDialog(container, "Project File Saved !", "Message", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(container, "Error: Project File NOT Saved !", "Error !", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
