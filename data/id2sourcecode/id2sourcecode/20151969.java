                public void actionPerformed(java.awt.event.ActionEvent e) {
                    String iconsetName = (String) iconsetComboBox.getSelectedItem();
                    LocalProperties.getInstance().setProperty(LocalProperties.PROP_ICONSET, iconsetName);
                    LocalProperties.getInstance().setProperty(LocalProperties.PROP_FONTSIZE, fontSizeTextField.getText());
                    Utils.changeChannelIconSet(iconsetName);
                    LocalProperties.getInstance().setProperty(LocalProperties.PROP_SYSTEM_LANGUAGE, String.valueOf(languageComboBox.getSelectedItem()));
                    LocalProperties.getInstance().storeLocalProps();
                    ChannelEditor.application.getChannelListingPanel().treeNodeStructureChanged(ChannelEditor.application.getChannelListingPanel().getRootNode());
                    setVisible(false);
                    dispose();
                }
