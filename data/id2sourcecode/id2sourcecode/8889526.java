    public final void testRead() throws URISyntaxException {
        final int MESSAGES = 10000;
        T4CLogGenerator totest = new T4CLogGenerator(MESSAGES, ResourceBundle.getBundle("barde_t4c", new Locale("fr")));
        long messages = 0;
        try {
            for (Message m = totest.read(); m != null; m = totest.read()) {
                assertNotNull(m.getAvatar());
                assertNotNull(m.getChannel());
                assertNotNull(m.getContent());
                assertNotNull(m.getDate());
                if (m.getChannel().matches(T4CClientReader.RC_CHANNEL_SYSTEM)) assertTrue(m.getContent().length() > 0);
                if (m.getChannel().matches(T4CClientReader.RC_CHANNEL_WHISP)) assertTrue(m.getAvatar().length() > 0);
                System.out.println(m.toString());
                messages++;
            }
        } catch (ParseException pe) {
            fail();
        } catch (IOException ioe) {
            fail();
        }
        assertEquals(MESSAGES, messages);
    }
