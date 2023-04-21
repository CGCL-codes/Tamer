    public void actionPerformed(ActionEvent e) {
        MindMapNodeModel node = (MindMapNodeModel) mMindMapController.getSelected();
        if (mMindMapController.getMap() == null || node == null || node.isRoot()) {
            mMindMapController.getFrame().err("Could not export branch.");
            return;
        }
        if (mMindMapController.getMap().getFile() == null) {
            mMindMapController.getFrame().out("You must save the current map first!");
            mMindMapController.save();
        }
        JFileChooser chooser;
        if (mMindMapController.getMap().getFile().getParentFile() != null) {
            chooser = new JFileChooser(mMindMapController.getMap().getFile().getParentFile());
        } else {
            chooser = new JFileChooser();
        }
        if (mMindMapController.getFileFilter() != null) {
            chooser.addChoosableFileFilter(mMindMapController.getFileFilter());
        }
        chooser.setSelectedFile(new File(Tools.getFileNameProposal(node) + freemind.main.FreeMindCommon.FREEMIND_FILE_EXTENSION));
        int returnVal = chooser.showSaveDialog(mMindMapController.getSelectedView());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File chosenFile = chooser.getSelectedFile();
            URL link;
            String ext = Tools.getExtension(chosenFile.getName());
            if (!ext.equals(freemind.main.FreeMindCommon.FREEMIND_FILE_EXTENSION_WITHOUT_DOT)) {
                chosenFile = new File(chosenFile.getParent(), chosenFile.getName() + freemind.main.FreeMindCommon.FREEMIND_FILE_EXTENSION);
            }
            try {
                link = Tools.fileToUrl(chosenFile);
            } catch (MalformedURLException ex) {
                JOptionPane.showMessageDialog(mMindMapController.getView(), "couldn't create valid URL!");
                return;
            }
            if (chosenFile.exists()) {
                int overwriteMap = JOptionPane.showConfirmDialog(mMindMapController.getView(), mMindMapController.getText("map_already_exists"), "FreeMind", JOptionPane.YES_NO_OPTION);
                if (overwriteMap != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            MindMapNodeModel parent = (MindMapNodeModel) node.getParentNode();
            try {
                String linkToNewMapString = Tools.toRelativeURL(Tools.fileToUrl(chosenFile), mMindMapController.getModel().getURL());
                mMindMapController.setLink(node, linkToNewMapString);
            } catch (MalformedURLException ex) {
                Resources.getInstance().logException(ex);
            }
            int nodePosition = parent.getChildPosition(node);
            mMindMapController.deleteNode(node);
            node.setParent(null);
            node.setFolded(false);
            ModeController newModeController = mMindMapController.getMode().createModeController();
            MindMapMapModel map = new MindMapMapModel(node, mMindMapController.getFrame(), newModeController);
            map.save(chosenFile);
            MindMapNode newNode = mMindMapController.addNewNode(parent, nodePosition, node.isLeft());
            mMindMapController.setNodeText(newNode, node.getText());
            try {
                String linkString = Tools.toRelativeURL(mMindMapController.getModel().getURL(), Tools.fileToUrl(chosenFile));
                mMindMapController.setLink(newNode, linkString);
            } catch (MalformedURLException ex) {
                Resources.getInstance().logException(ex);
            }
            mMindMapController.newMap(map);
        }
    }
