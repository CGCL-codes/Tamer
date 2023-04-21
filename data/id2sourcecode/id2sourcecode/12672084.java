    private JComponent createLabelChannelId(final Show show, final int id) {
        final JLabel label = new JLabel("" + (id + 1));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        Channel channel = getShow().getChannels().get(id);
        final Level inputLevel = getShow().getChannelInputs().get(id);
        final Level channelLevel = channel.getLevel();
        LevelListener listener = new LevelListener() {

            public void levelChanged() {
                if (inputLevel.getIntValue() == channelLevel.getIntValue()) {
                    label.setBackground(Color.white);
                } else {
                    label.setBackground(null);
                }
            }
        };
        inputLevel.add(listener);
        channelLevel.add(listener);
        return label;
    }
