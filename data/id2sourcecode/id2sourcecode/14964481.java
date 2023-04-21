    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(reloadFileButton)) {
            refreshAndSelect(true);
        } else if (e.getSource().equals(viewTypeCombo)) {
            treeModel.setViewType(viewTypeCombo.getSelectedIndex());
            treeModel.modelChanged();
            refreshTree(false);
        } else if (e.getSource().equals(saveButton)) {
            if (currentNode != null) {
                updateElement(currentNode);
            }
            treeModel.writeToXmlFile();
            refreshAndSelect(true);
        } else if (e.getSource().equals(deleteButton)) {
            if (tree.getSelectionCount() > 1) {
                TreePath[] paths = tree.getSelectionPaths();
                for (int i = 0; i < paths.length; i++) {
                    ElementNode temp = (ElementNode) paths[i].getLastPathComponent();
                    ElementNode parent = (ElementNode) temp.getParent();
                    if (parent != null) {
                        parent.removeElement(temp);
                        treeModel.modelChanged();
                    }
                }
            } else {
                ElementNode parent = (ElementNode) currentNode.getParent();
                if (parent != null) {
                    parent.removeElement(currentNode);
                    treeModel.modelChanged();
                }
            }
            refreshTree(false);
        } else if (e.getSource().equals(loadFile)) {
            JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
            chooser.setDialogTitle("Select XML report file");
            ExtentionsFileFilter ff = new ExtentionsFileFilter();
            ff.setDescription("XML files");
            ff.addExtention("xml");
            chooser.setFileFilter(ff);
            int returnVal = chooser.showOpenDialog(null);
            if (returnVal != JFileChooser.APPROVE_OPTION) {
                return;
            }
            File file = chooser.getSelectedFile();
            try {
                FileUtils.copyFile(file, new File(reportFileName));
                refreshTree(true);
            } catch (Exception e1) {
                log.log(Level.WARNING, "fail to load model", e1);
            }
        }
    }
