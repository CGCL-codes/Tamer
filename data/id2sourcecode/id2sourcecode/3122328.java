    private void saveGrpButton_actionPerformed(ActionEvent e) {
        DefaultListModel groupModel = new DefaultListModel();
        JList groupGenes = new JList();
        groupGenes.setModel(groupModel);
        try {
            TreePath allselected[] = firsttree.getSelectionPaths();
            for (int count = 0; count < allselected.length; count++) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) allselected[count].getLastPathComponent();
                GrpFile group = formGroup(node);
                Object[] o = group.getGroup();
                if (o.length > 0) {
                    for (int i = 0; i < o.length; i++) {
                        groupModel.addElement(o[i].toString());
                    }
                }
            }
        } catch (NullPointerException e1) {
            JOptionPane.showMessageDialog(this, "You Must Select A Group To Save!", "Alert", JOptionPane.ERROR_MESSAGE);
        }
        String s = JOptionPane.showInputDialog(parentFrame, "Enter The Group Name:");
        if (s != null) {
            GrpFile newGrp = new GrpFile(s);
            for (int i = 0; i < groupModel.size(); i++) {
                newGrp.addOne(groupModel.elementAt(i));
            }
            if (!s.endsWith(".grp")) s += ".grp";
            newGrp.setExpFile(exp.getName());
            try {
                File file = new File(project.getPath() + exp.getName() + File.separator + s);
                int result = JOptionPane.YES_OPTION;
                if (file.exists()) {
                    result = JOptionPane.showConfirmDialog(parentFrame, "The file " + file.getPath() + " already exists.  Overwrite this file?", "Overwrite File?", JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.YES_OPTION) file.delete();
                }
                if (result == JOptionPane.YES_OPTION) newGrp.writeGrpFile(project.getPath() + exp.getName() + File.separator + s);
            } catch (DidNotFinishException e2) {
                JOptionPane.showMessageDialog(parentFrame, "Error Writing Group File");
            }
            project.addFile(exp.getName() + File.separator + s);
        }
    }
