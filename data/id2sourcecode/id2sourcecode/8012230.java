    private JLabel getChannelLBL() {
        if (channelLBL == null) {
            channelLBL = new JLabel();
            channelLBL.setText("MIDI Channel");
            channelLBL.setBounds(new Rectangle(15, 105, 116, 21));
        }
        return channelLBL;
    }
