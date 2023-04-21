    public static void performInInfo(final IModuleStorage.Info info, final TVProgramme programme) {
        if (!info.channelsList.contains(programme.getChannel().getID())) {
            info.channelsList.add(new TVChannelsSet.Channel(programme.getChannel().getID(), programme.getChannel().getDisplayName()));
        }
        if (info.minDate > programme.getStart()) {
            info.minDate = programme.getStart();
        }
        if (info.maxDate < programme.getEnd()) {
            info.maxDate = programme.getEnd();
        }
    }
