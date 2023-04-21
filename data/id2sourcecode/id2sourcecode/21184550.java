    public Map<String, List> getMeetMeUsers() {
        IScope scope = Red5.getConnectionLocal().getScope();
        logger.debug("GetMeetmeUsers request for room[" + scope.getName() + "]");
        Map<String, List> usersMap = new HashMap<String, List>();
        ;
        if (hasSharedObject(scope, "meetMeUsersSO")) {
            logger.info("MeetMe::service - Getting current users for room " + scope.getName());
            Collection<MeetMeUser> currentUsers = roomListener.getCurrentUsers(scope.getName());
            logger.info("MeetMe::service - There are " + currentUsers.size() + " current users...");
            for (Iterator it = currentUsers.iterator(); it.hasNext(); ) {
                MeetMeUser oneUser = (MeetMeUser) it.next();
                List<Object> aUser = new ArrayList<Object>();
                aUser.add(oneUser.getUserNumber());
                aUser.add(oneUser.getChannel().getCallerId().getName());
                aUser.add(oneUser.getChannel().getCallerId().getNumber());
                aUser.add(new Boolean(oneUser.isMuted()));
                aUser.add(new Boolean(oneUser.isTalking()));
                usersMap.put(oneUser.getUserNumber().toString(), aUser);
            }
        }
        logger.info("MeetMe::service - Sending " + usersMap.size() + " current users...");
        return usersMap;
    }
