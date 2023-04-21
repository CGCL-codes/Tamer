    public Collection getProgrammes(final TVChannelsSet.Channel channel) throws Exception {
        final IModuleStorage.Info filter = info.cloneInfo();
        filter.channelsList.getChannels().clear();
        filter.channelsList.getChannels().add(channel);
        final TVData data = Application.getInstance().getDataStorage().get(filter);
        final Set programmes = data.get(channel.getChannelID()).getProgrammes();
        if (selectedOnly) {
            for (Iterator it = programmes.iterator(); it.hasNext(); ) {
                final TVProgramme programme = (TVProgramme) it.next();
                if (!Application.getInstance().getReminders()[0].isSelected(programme)) {
                    it.remove();
                }
            }
        }
        return programmes;
    }
