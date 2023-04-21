    public boolean existsSessionPart(Date startDate, String channel, long sleepID) {
        Set<SessionPart> parts = findSessionParts(sleepID);
        for (SessionPart sp : parts) {
            if (sp.getStart().equals(startDate) && sp.getChannel().equals(channel)) {
                return true;
            }
        }
        return false;
    }
