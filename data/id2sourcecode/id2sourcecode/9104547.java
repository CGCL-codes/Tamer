    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (propertyChangeEvent.getSource() instanceof MeetMeUser) {
            MeetMeUser user = (MeetMeUser) propertyChangeEvent.getSource();
            System.out.println(user.getChannel().getCallerId().getName() + " " + user.getChannel().getCallerId().getNumber());
        }
    }
