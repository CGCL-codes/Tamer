                public void actionPerformed(java.awt.event.ActionEvent e) {
                    DefaultListModel listModel = (DefaultListModel) jList.getModel();
                    Enumeration enumer = listModel.elements();
                    while (enumer.hasMoreElements()) {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumer.nextElement();
                        ChannelEditor.application.getChannelDeletedPanel().addElement(node);
                    }
                    listModel.removeAllElements();
                }
