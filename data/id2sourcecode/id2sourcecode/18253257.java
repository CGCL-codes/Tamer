    public void testGetNewMessagesWorks() {
        final String gSession = g.getSession();
        try {
            if (cm.getNewMessages("user1", gSession) == null) {
                fail("getNewMessages() returned null while preparing the real test");
            }
        } catch (AccessDeniedException e1) {
            fail("Got AccessDeniedException while preparing the real test");
        }
        testPostMessageWorks();
        final List<ChatMessage> msgs = g.getChannel().getMessages();
        String[] expected = { msgs.get(msgs.size() - 1).toString() };
        try {
            String[] result = cm.getNewMessages("user1", gSession);
            assertTrue("Unexpected getNewMessages() result, was " + Arrays.deepToString(result) + ", not " + Arrays.deepToString(expected), Arrays.deepEquals(result, expected));
        } catch (AccessDeniedException e) {
            fail("Got access denied exception.");
        }
    }
