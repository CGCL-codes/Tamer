    public void testGetNewMessagesIncrementally() {
        testGetNewMessagesWorks();
        try {
            assertTrue("postMessage() returned false", cm.postMessage("user1", g.getSession(), "blubb"));
        } catch (AccessDeniedException e) {
            fail("Got access denied exception while posting another message.");
            return;
        }
        final List<ChatMessage> msgs = g.getChannel().getMessages();
        String[] expected = { msgs.get(msgs.size() - 1).toString() };
        try {
            String[] result = cm.getNewMessages("user1", g.getSession());
            assertTrue("Unexpected getNewMessages() result, was " + Arrays.deepToString(result) + ", not " + Arrays.deepToString(expected), Arrays.deepEquals(result, expected));
        } catch (AccessDeniedException e) {
            fail("Got access denied exception while retrieving new messages.");
        }
    }
