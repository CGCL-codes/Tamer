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
