    public void actionPerformed(ActionEvent e) {
        File channelFile = null;
        final JFileChooser fc = new JFileChooser();
        if (lastDirectory != null) {
            fc.setCurrentDirectory(lastDirectory);
        }
        int ret = fc.showOpenDialog(ChannelEditor.application);
        if (ret == JFileChooser.APPROVE_OPTION) {
            channelFile = fc.getSelectedFile();
        }
        if (channelFile != null) {
            lastDirectory = channelFile.getParentFile();
            try {
                Map aliasMap = Utils.buildAliasMap(new FileReader(channelFile));
                int aliasCount = aliasMap.size();
                int aliasAssigned = 0;
                if (!aliasMap.isEmpty()) {
                    DefaultMutableTreeNode rootNode = ChannelEditor.application.getChannelListingPanel().getRootNode();
                    Enumeration enumer = rootNode.preorderEnumeration();
                    while (enumer.hasMoreElements()) {
                        DefaultMutableTreeNode mutableNode = (DefaultMutableTreeNode) enumer.nextElement();
                        if (mutableNode.getUserObject() instanceof Channel) {
                            Channel channel = (Channel) mutableNode.getUserObject();
                            String alias = (String) aliasMap.get(channel.getId());
                            if (alias != null) {
                                channel.setAlias(alias);
                                aliasAssigned++;
                            }
                        }
                    }
                }
                JOptionPane.showMessageDialog(ChannelEditor.application, Messages.getString("ImportAliasAction.2") + aliasCount + Messages.getString("ImportAliasAction.3") + channelFile.getAbsolutePath() + Messages.getString("ImportAliasAction.4") + aliasAssigned + Messages.getString("ImportAliasAction.5"));
            } catch (FileNotFoundException fnfe) {
                JOptionPane.showMessageDialog(ChannelEditor.application, Messages.getString("ImportAliasAction.6") + channelFile.getAbsolutePath() + Messages.getString("ImportAliasAction.7") + fnfe.getMessage());
                channelFile = null;
            }
        }
    }
