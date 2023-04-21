    private void initPresets(final JPanel panel, final GridBagConstraints c) {
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        if (includeOutputPanel()) {
            panel.add(new ControlOutputPanel(getContext().getLanbox().getMixer().getLevels()), c);
            c.gridy++;
        }
        if (includeOutputPanel()) {
            List<Channel> subset = new ArrayList<Channel>();
            Channels channels = getShow().getChannels();
            for (int i = 0; i < Dmx.CONTROL_CHANNELS; i++) {
                Channel channel = channels.get(i);
                subset.add(channel);
            }
            Channels controlChannels = new Channels(subset);
            panel.add(new ControlLabelPanel(controlChannels), c);
        } else {
            panel.add(new ControlLabelPanel(getShow().getSubmasters()), c);
        }
        c.gridy++;
        panel.add(createPresetPanel(getControl().getPresetA()), c);
        c.gridy++;
        panel.add(createPresetPanel(getControl().getPresetB()), c);
    }
