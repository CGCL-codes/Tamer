    private JButton getJButton() {
        if (jButton == null) {
            jButton = new JButton();
            jButton.setText(Messages.getString("CreateChannelDialog.2"));
            jButton.setIcon(new ImageIcon(getClass().getResource("/org/javalobby/icons/20x20/New.gif")));
            jButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    Channel channel = getChannelPropertyPanel().getChannel();
                    if (Utils.isEmpty(channel.getName())) {
                        JOptionPane.showMessageDialog(CreateChannelDialog.this, Messages.getString("CreateChannelDialog.4"));
                        return;
                    }
                    result = RESULT_CREATE;
                    setVisible(false);
                    dispose();
                }
            });
        }
        return jButton;
    }
