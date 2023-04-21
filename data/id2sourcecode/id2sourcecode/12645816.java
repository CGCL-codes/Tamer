    private void constructChannelSelect() {
        channelSelect = new JComboBox();
        for (int i = 0; i < 16; i++) {
            channelSelect.addItem("ch " + (i + 1));
        }
        channelSelect.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                part.getPart().setChannel(channelSelect.getSelectedIndex() + 1);
            }
        });
        if (part.getPart().getChannel() > 16 || part.getPart().getChannel() < 1) {
            PO.p("channel out of bounds warning " + part.getPart().getChannel());
        }
        channelSelect.setSelectedIndex(part.getPart().getChannel() - 1);
    }
