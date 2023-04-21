    private void constructChannelSelect() {
        channelSelect = new JComboBox();
        for (int i = 0; i < 16; i++) {
            channelSelect.addItem("ch " + (i + 1));
        }
        channelSelect.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                part.setChannel(channelSelect.getSelectedIndex() + 1);
            }
        });
        channelSelect.setPreferredSize(new Dimension(100, 24));
        if (part.getChannel() > 16 || part.getChannel() < 1) {
            PO.p("channel out of bounds warning " + part.getChannel());
        }
        channelSelect.setSelectedIndex(part.getChannel() - 1);
    }
