    public void actionPerformed(ActionEvent e) {
        if (ChannelEditor.application.isModified()) {
            int result = JOptionPane.showConfirmDialog(ChannelEditor.application, Messages.getString("OpenAction.2"), Messages.getString("OpenAction.3"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (result == JOptionPane.NO_OPTION) {
                return;
            }
        }
        File channelFile = null;
        if (this.openFile != null) {
            channelFile = this.openFile;
        } else {
            final JFileChooser fc = new JFileChooser();
            if (lastDirectory != null) {
                fc.setCurrentDirectory(lastDirectory);
            }
            FileFilter fFilter = new FileFilter() {

                public boolean accept(File f) {
                    if (f.isDirectory()) {
                        return true;
                    }
                    if (f.getName().endsWith(".conf")) {
                        return true;
                    }
                    return false;
                }

                public String getDescription() {
                    return Messages.getString("OpenAction.5");
                }
            };
            fc.setFileFilter(fFilter);
            int ret = fc.showOpenDialog(ChannelEditor.application);
            if (ret == JFileChooser.APPROVE_OPTION) {
                channelFile = fc.getSelectedFile();
            }
        }
        if (channelFile != null) {
            lastDirectory = channelFile.getParentFile();
            try {
                DefaultMutableTreeNode rootNode = Utils.buildChannelTree(new FileReader(channelFile), channelFile.getName());
                ChannelEditor.application.getChannelListingPanel().setDefaultTreeModel(rootNode);
                Utils.setLastOpenedFile(channelFile);
                ChannelEditor.application.refreshLastOpenedFiles();
            } catch (FileNotFoundException fnfe) {
                JOptionPane.showMessageDialog(ChannelEditor.application, Messages.getString("OpenAction.6") + channelFile.getAbsolutePath() + Messages.getString("OpenAction.7") + fnfe.getMessage());
                channelFile = null;
            }
        }
        ChannelEditor.application.setChannelFile(channelFile);
    }
