    public void testPOP3ClientStateTransition() throws Exception {
        POP3Client pop = new POP3Client();
        assertEquals(110, pop.getDefaultPort());
        assertEquals(POP3.DISCONNECTED_STATE, pop.getState());
        assertNull(pop._reader);
        assertNotNull(pop._replyLines);
        pop.connect(mailhost);
        assertEquals(POP3.AUTHORIZATION_STATE, pop.getState());
        pop.login(user, password);
        assertEquals(POP3.TRANSACTION_STATE, pop.getState());
        pop.noop();
        assertEquals(POP3.TRANSACTION_STATE, pop.getState());
        pop.status();
        assertEquals(POP3.TRANSACTION_STATE, pop.getState());
        POP3MessageInfo[] msg = pop.listMessages();
        if (msg.length > 0) {
            pop.deleteMessage(1);
            assertEquals(POP3.TRANSACTION_STATE, pop.getState());
            pop.reset();
            assertEquals(POP3.TRANSACTION_STATE, pop.getState());
            pop.listMessage(1);
            assertEquals(POP3.TRANSACTION_STATE, pop.getState());
            pop.listMessages();
            assertEquals(POP3.TRANSACTION_STATE, pop.getState());
            pop.listUniqueIdentifier(1);
            assertEquals(POP3.TRANSACTION_STATE, pop.getState());
            pop.listUniqueIdentifiers();
            assertEquals(POP3.TRANSACTION_STATE, pop.getState());
            Reader r = pop.retrieveMessage(1);
            assertEquals(POP3.TRANSACTION_STATE, pop.getState());
            while (!r.ready()) {
                Thread.sleep(10);
            }
            r.close();
            r = null;
            r = pop.retrieveMessageTop(1, 10);
            assertEquals(POP3.TRANSACTION_STATE, pop.getState());
            while (!r.ready()) {
                Thread.sleep(10);
            }
            r.close();
            r = null;
        }
        pop.logout();
        assertEquals(POP3.UPDATE_STATE, pop.getState());
    }
