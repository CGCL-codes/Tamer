    private void submitValues_actionPerformed() {
        if (channelElement instanceof Channel) {
            Channel channel = (Channel) channelElement;
            String name = nameTextField.getText();
            String bouqet = bouqetTextField.getText();
            if (Utils.isEmpty(bouqet)) {
                channelElement.setName(name);
            } else {
                channelElement.setName(name + ";" + bouqet);
            }
            channel.setFrequenz(frequenzTextField.getText());
            channel.setParameter(parameterTextField.getText());
            channel.setSource(sourceTextField.getText());
            channel.setSymbolrate(symbolrateTextField.getText());
            channel.setVPid(vpidTextField.getText());
            channel.setAPid(apidTextField.getText());
            channel.setTPid(tpidTextField.getText());
            channel.setCaId(caidTextField.getText());
            channel.setSid(sidTextField.getText());
            channel.setNid(nidTextField.getText());
            channel.setTid(tidTextField.getText());
            channel.setRid(ridTextField.getText());
            channel.setAlias(aliasTextField.getText());
        } else if (channelElement instanceof ChannelCategory) {
            ChannelCategory channelCategory = (ChannelCategory) channelElement;
            channelCategory.setName(nameTextField.getText());
            int numberAt = 0;
            try {
                numberAt = Integer.parseInt(startnrTextField.getText());
            } catch (NumberFormatException e) {
            }
            channelCategory.setNumberAt(numberAt);
        } else {
            channelElement.setName(nameTextField.getText());
        }
        TreePath treePath = ChannelEditor.application.getChannelListingPanel().getLeadSelectionPath();
        if (treePath != null) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
            ChannelEditor.application.getChannelListingPanel().treeNodeChanged(node);
        }
        jButton.setEnabled(false);
        ChannelEditor.application.setModified(true);
    }
