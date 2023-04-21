    public Collection getProgrammes(Channel channel) throws Exception {
        if (channelsList && !info.channelsList.contains(channel.getChannelID())) {
            return new TreeSet<TVProgramme>();
        }
        final IModuleStorage.Info filter = info.cloneInfo();
        filter.channelsList.getChannels().clear();
        filter.channelsList.getChannels().add(channel);
        final TVData data = storage.get(filter);
        final Set programmes = data.get(channel.getChannelID()).getProgrammes();
        for (Iterator it = programmes.iterator(); it.hasNext(); ) {
            final TVProgramme programme = (TVProgramme) it.next();
            if ((selectedOnly && !application.getReminders()[0].isSelected(programme)) || (todayOnly && (programme.getEnd() < info.minDate || programme.getStart() > info.maxDate))) {
                it.remove();
            }
        }
        return programmes;
    }
