    private static void fillUsersContainer(IndexedContainer container, Set<SNTMeetMeUser> users) {
        container.addContainerProperty(PROPERTY_USERID, String.class, null);
        container.addContainerProperty(PROPERTY_CALLERNUM, String.class, null);
        container.addContainerProperty(PROPERTY_CALLERID, String.class, null);
        container.addContainerProperty(PROPERTY_CHANNEL, String.class, null);
        container.addContainerProperty(PROPERTY_MUTE, Boolean.class, null);
        container.addContainerProperty(PROPERTY_DURATION, String.class, null);
        for (SNTMeetMeUser user : users) {
            Item item = container.addItem(user.getUserId());
            item.getItemProperty(PROPERTY_USERID).setValue(user.getUserId());
            item.getItemProperty(PROPERTY_CALLERNUM).setValue(user.getCallerNum());
            item.getItemProperty(PROPERTY_CALLERID).setValue(user.getCallerId());
            item.getItemProperty(PROPERTY_CHANNEL).setValue(user.getChannel());
            item.getItemProperty(PROPERTY_MUTE).setValue(user.isMute());
            item.getItemProperty(PROPERTY_DURATION).setValue(user.getDate());
        }
        container.sort(new Object[] { PROPERTY_CALLERID }, new boolean[] { true });
    }
