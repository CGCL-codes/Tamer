    private JButton getJButton() {
        if (jButton == null) {
            jButton = new JButton();
            jButton.setIcon(new ImageIcon(getClass().getResource("/org/javalobby/icons/20x20/Delete.gif")));
            jButton.setText(Messages.getString("ChannelParkingPanel.2"));
            jButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    DefaultListModel listModel = (DefaultListModel) jList.getModel();
                    Enumeration enumer = listModel.elements();
                    while (enumer.hasMoreElements()) {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumer.nextElement();
                        ChannelEditor.application.getChannelDeletedPanel().addElement(node);
                    }
                    listModel.removeAllElements();
                }
            });
        }
        return jButton;
    }
