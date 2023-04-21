    private void addLine(final JPanel panel, final GridBagConstraints c, final int index) {
        Channel channel = getShow().getChannels().get(index);
        Level inputLevel = getShow().getInputs().get(index);
        Level channelLevel = channel.getLevel();
        c.gridy = index;
        c.weightx = 1;
        c.gridx = 0;
        panel.add(createLabelChannelName(channel), c);
        c.weightx = 0;
        c.gridx = 1;
        panel.add(createLabelChannelId(getShow(), index), c);
        c.gridx = 2;
        panel.add(new LevelIndicatorHorizontal(inputLevel), c);
        c.gridx = 3;
        panel.add(new LevelLabel(inputLevel), c);
        c.gridx = 4;
        panel.add(new LevelIndicatorHorizontal(channelLevel), c);
        c.gridx = 5;
        panel.add(new LevelLabel(channelLevel), c);
    }
