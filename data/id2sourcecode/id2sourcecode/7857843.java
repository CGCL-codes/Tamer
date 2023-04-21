    public void onInviteSent(InviteSentEvent inviteSentEvent) {
        System.out.println("onInviteSent");
        assertEquals("onInviteSent(): user", "Scurvy", inviteSentEvent.getNick());
        assertEquals("onInviteSent(): channel", "#sharktest", inviteSentEvent.getChannel());
    }
