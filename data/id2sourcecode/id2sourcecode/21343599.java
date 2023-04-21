    private void addTracker(final JPanel panel, final GridBagConstraints c, final int index) {
        Channel channel = getShow().getChannels().get(index);
        Tracker tracker = getShow().getTrackerEngine().get(index);
        c.gridy = index;
        c.weightx = 1;
        c.gridx = 0;
        panel.add(createLabelChannelName(channel), c);
        c.weightx = 0;
        c.gridx = 1;
        panel.add(createLabelChannelId(getShow(), index), c);
        c.gridx = 2;
        panel.add(createLevelIndicator(tracker.getLevel(), index), c);
        c.gridx = 3;
        panel.add(new LevelLabel(tracker.getLevel()), c);
    }
