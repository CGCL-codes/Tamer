    protected void prepareChannelsSetList() {
        panel.getComboChannelsSet().removeItemListener(handlers.comboChannelsSetItemListener);
        int itemCount = panel.getComboChannelsSet().getItemCount();
        for (int i = 0; i < itemCount; i++) {
            panel.getComboChannelsSet().removeItemAt(0);
        }
        panel.getComboChannelsSet().insertItemAt(getLocalizer().getString("all_channels"), 0);
        for (int i = 0; i < Application.getInstance().getChannelsSetsList().size(); i++) {
            TVChannelsSet cs = (TVChannelsSet) Application.getInstance().getChannelsSetsList().get(i);
            panel.getComboChannelsSet().addItem(cs.getName());
        }
        panel.getComboChannelsSet().addItem(getLocalizer().getString("edit_channels_sets"));
        TVChannelsSet cs = getChannelsSetByName(config.currentChannelSetName);
        if (cs == null) {
            config.currentChannelSetName = null;
            panel.getComboChannelsSet().setSelectedIndex(0);
        } else {
            panel.getComboChannelsSet().setSelectedIndex(Application.getInstance().getChannelsSetsList().indexOf(cs) + 1);
        }
        panel.getComboChannelsSet().addItemListener(handlers.comboChannelsSetItemListener);
    }
