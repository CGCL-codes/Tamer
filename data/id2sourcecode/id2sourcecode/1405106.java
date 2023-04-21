    @Test
    public void changePasswordTest() throws Exception {
        connection.login("accountManager@jabber.org", "test");
        service.changePassword("password");
        connection.disconnect();
        connection.connect("jabber.org");
        service = connection.getAccountManagerService();
        final PresenceListener listener = context.mock(PresenceListener.class);
        try {
            PresenceService presence = connection.login("accountManager@jabber.org", "password");
            presence.addPresenceListener(listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
        context.checking(new Expectations() {

            {
                allowing(listener).updateEvent(with(any(String.class)), with(any(String.class)));
            }
        });
        Assert.assertTrue(connection.isAuthenticated());
        service.changePassword("test");
        connection.disconnect();
        connection.connect("jabber.org");
        service = connection.getAccountManagerService();
        try {
            PresenceService presence = connection.login("accountManager@jabber.org", "test");
            presence.addPresenceListener(listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
        context.checking(new Expectations() {

            {
                allowing(listener).updateEvent(with(any(String.class)), with(any(String.class)));
            }
        });
        Assert.assertTrue(connection.isAuthenticated());
    }
