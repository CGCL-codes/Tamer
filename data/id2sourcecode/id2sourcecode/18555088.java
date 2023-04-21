    private JComponent createLabelChannelId(final Show show, final int id) {
        final JTextField label = new JTextField("" + (id + 1), 2);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setEditable(false);
        label.setBorder(null);
        label.setBackground(null);
        label.setHighlighter(null);
        Channel channel = getShow().getChannels().get(id);
        final Level inputLevel = getShow().getInputs().get(id);
        final Level channelLevel = channel.getLevel();
        inputLevel.add(new LevelListener() {

            public void levelChanged() {
                if (inputLevel.getIntValue() == channelLevel.getIntValue()) {
                    label.setBackground(Color.white);
                } else {
                    label.setBackground(null);
                }
            }
        });
        channelLevel.add(new LevelListener() {

            public void levelChanged() {
                if (inputLevel.getIntValue() == channelLevel.getIntValue()) {
                    label.setBackground(Color.white);
                } else {
                    label.setBackground(null);
                }
            }
        });
        return label;
    }
