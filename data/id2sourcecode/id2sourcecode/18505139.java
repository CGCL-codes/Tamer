    private void saveAsMenu_actionPerformed(ActionEvent e) {
        String s = JOptionPane.showInputDialog(this, "Enter The Group Name:");
        if (s != null) {
            group = new GrpFile(s);
            for (int i = 0; i < groupModel.size(); i++) {
                group.addOne(groupModel.elementAt(i));
            }
            if (!s.endsWith(".grp")) s += ".grp";
            group.setExpFile(exp.getName());
            try {
                File file = new File(p.getPath() + exp.getName() + File.separator + s);
                int result = JOptionPane.YES_OPTION;
                if (file.exists()) {
                    result = JOptionPane.showConfirmDialog(parentFrame, "The file " + file.getPath() + " already exists.  Overwrite this file?", "Overwrite File?", JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.YES_OPTION) file.delete();
                }
                if (result == JOptionPane.YES_OPTION) {
                    group.writeGrpFile(p.getPath() + exp.getName() + File.separator + s);
                    p.addFile(exp.getName() + File.separator + s);
                    saveMenu.setEnabled(true);
                    this.setTitle(group.getTitle());
                    updateParentFrame();
                }
            } catch (DidNotFinishException e2) {
                JOptionPane.showMessageDialog(this, "Error Writing Group File");
            }
        }
    }
