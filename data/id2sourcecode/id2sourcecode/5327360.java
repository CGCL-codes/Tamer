    protected long importNewSleepSessionData(SleepSession sleep) throws UserException {
        sessionDAO.put(sleep);
        SessionPart part = new SessionPart(parser.getPartStartDate(), parser.getChannel(), sleep.getId());
        return importNewSessionPartData(part);
    }
