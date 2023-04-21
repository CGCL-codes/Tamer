    public boolean showProgramme(TVProgramme programme) {
        return (this.arAllowedChannelIds.size() == 0) || this.arAllowedChannelIds.contains(programme.getChannel().getID());
    }
