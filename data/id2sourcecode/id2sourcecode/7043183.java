    public boolean matches(final TVProgramme programme) {
        if (channelID.equals(programme.getChannel().getID()) && (programmeTime == programme.getStart())) {
            return true;
        }
        return false;
    }
