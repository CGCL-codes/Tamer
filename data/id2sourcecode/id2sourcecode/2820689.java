    private void updateConsoleChannelsUi(ConsoleSpec spec) {
        channelsLabel.setEnabled(spec != null);
        channelsField.setText(spec == null ? "" : makeChannelListDisplayString(spec.getChannels()));
        channelsField.setEnabled(spec != null);
        channelsField.setEditable(spec != null);
        addRemoveChannels.getAddButton().setEnabled(spec != null);
        addRemoveChannels.getRemoveButton().setEnabled((spec != null) && !spec.getChannels().isEmpty());
    }
