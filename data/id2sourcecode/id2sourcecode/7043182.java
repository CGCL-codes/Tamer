    public ManualSelection(final TVProgramme programme, final boolean selected) {
        this.channelID = programme.getChannel().getID();
        this.programmeTime = programme.getStart();
        this.selected = selected;
    }
