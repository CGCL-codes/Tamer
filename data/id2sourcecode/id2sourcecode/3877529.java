    public String getChannel(long sessionPartID) {
        SessionPart sp = partDAO.get(sessionPartID);
        return sp.getChannel();
    }
