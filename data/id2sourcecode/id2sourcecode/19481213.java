    private void copy(MeetMeUser userToCopy) {
        callerIdName = userToCopy.getChannel().getCallerId().getName();
        ;
        callerIdNumber = userToCopy.getChannel().getCallerId().getNumber();
        roomNumber = userToCopy.getRoom().getRoomNumber();
        dateJoined = userToCopy.getDateJoined();
        dateLeft = userToCopy.getDateLeft();
        userNumber = userToCopy.getUserNumber();
        muted = userToCopy.isMuted();
        talking = userToCopy.isTalking();
    }
