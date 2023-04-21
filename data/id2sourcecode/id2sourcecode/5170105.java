    private void saveGrpChoice_actionPerformed(ActionEvent e) {
        DefaultListModel groupModel = new DefaultListModel();
        JList groupGenes = new JList();
        groupGenes.setModel(groupModel);
        Object[] o = theDisplay.getAllSelected();
        if (o.length > 0) {
            for (int i = 0; i < o.length; i++) {
                groupModel.addElement(o[i].toString());
            }
            String s = JOptionPane.showInputDialog(parent, "Enter The Group Name:");
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
                        result = JOptionPane.showConfirmDialog(parent, "The file " + file.getPath() + " already exists.  Overwrite this file?", "Overwrite File?", JOptionPane.YES_NO_OPTION);
                        if (result == JOptionPane.YES_OPTION) file.delete();
                    }
                    if (result == JOptionPane.YES_OPTION) newGrp.writeGrpFile(project.getPath() + exp.getName() + File.separator + s);
                } catch (DidNotFinishException e2) {
                    JOptionPane.showMessageDialog(parent, "Error Writing Group File");
                }
                project.addFile(exp.getName() + File.separator + s);
            }
        } else {
            JOptionPane.showMessageDialog(parent, "No Genes Selected");
        }
    }
