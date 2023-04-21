    public void actionPerformed(ActionEvent arg0) {
        Component parent = getParentFrame();
        VerifyUnreachableRulesAction a = new VerifyUnreachableRulesAction(tree);
        a.actionPerformed(arg0);
        File file = ToxTreeActions.selectFile(parent, MolFileFilter.toxTree_ext_save, MolFileFilter.toxTree_ext_descr_save, false);
        if (file == null) return;
        try {
            if (file.exists() && (JOptionPane.showConfirmDialog(parent, "File " + file.getAbsolutePath() + " already exists.\nOverwrite?", "Please confirm", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.CANCEL_OPTION)) return;
            FileOutputStream out = new FileOutputStream(file);
            exportRules(out, file.getAbsolutePath());
            tree.setTitle(file.getAbsolutePath());
            tree.setModified(false);
            try {
                out.close();
            } catch (IOException x) {
                JOptionPane.showMessageDialog(parent, "Error on saving rules", x.getMessage(), JOptionPane.ERROR_MESSAGE);
            }
        } catch (FileNotFoundException x) {
            JOptionPane.showMessageDialog(parent, "Error on saving rules", x.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }
