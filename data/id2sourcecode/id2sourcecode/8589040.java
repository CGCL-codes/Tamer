    public void onNewMeetMeUser(MeetMeUser user) {
        log.info("New user joined meetme room: " + user.getRoom() + " " + user.getChannel().getCallerId().getName());
        String roomNumber = user.getRoom().getRoomNumber();
        if (meetMeSOs.containsKey(roomNumber)) {
            ISharedObject so = meetMeSOs.get(roomNumber);
            user.addPropertyChangeListener(new ParticipantPropertyChangeListener(so));
            List<Object> args = new ArrayList<Object>();
            args.add(user.getUserNumber());
            args.add(user.getChannel().getCallerId().getName());
            args.add(user.getChannel().getCallerId().getNumber());
            args.add(new Boolean(user.isMuted()));
            args.add(new Boolean(user.isTalking()));
            so.sendMessage("userJoin", args);
        }
    }
