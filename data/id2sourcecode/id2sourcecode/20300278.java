    private void newLangButtonActionPerformed(ActionEvent evt) {
        TreeNode<LanguageProperties> root = project.getRoot();
        if (root != null) {
            NewLanguageDlg dialog = new NewLanguageDlg(this);
            String[] languageCodes = dialog.execute();
            if (languageCodes != null) {
                String country = languageCodes[0];
                if (country == null || country.length() != 2 || !Utils.isLower(country)) {
                    NotificationFactory.createInfoPane(this, "Country field is not given or not 2 two small letter term.");
                    return;
                }
                LanguageProperties language = new LanguageProperties();
                language.setLanguageCodes(languageCodes);
                language.setClearName();
                boolean do_it = true;
                if (LanguageTreeManager.contains(root, language)) {
                    int ans = JOptionPane.showConfirmDialog(this, "Language already existing! Overwrite?", "New Language", JOptionPane.YES_NO_OPTION);
                    if (ans == JOptionPane.NO_OPTION) {
                        do_it = false;
                    }
                }
                if (do_it == true) {
                    LanguageTreeManager.insertLangPropInTree(root, language);
                    LanguageTreeManager.sortTreeForClearNames(root);
                    languageTree.setSelectionRow(0);
                    LanguageTreeModel langTreeModel = (LanguageTreeModel) languageTree.getModel();
                    langTreeModel.structureChanged();
                }
            }
        }
    }
