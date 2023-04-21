    public void testBasicFailuresDeserialization() throws Exception {
        InputStream in = new ByteArrayInputStream(FAILURES_BASIC.getBytes(UTF_8));
        Failures failures = parser.readFailures(in);
        assertEquals(3, failures.getRecipients().length);
        final Recipient[] recipients = failures.getRecipients();
        Recipient alice = recipients[0];
        assertEquals("alice@volantis.com", alice.getAddress().getValue());
        assertEquals("smtp", alice.getChannel());
        assertEquals("Nokia-6600", alice.getDeviceName());
        assertEquals("Default type has been assigned", RecipientType.TO, alice.getRecipientType());
        Recipient bob = recipients[1];
        assertEquals("bob@volantis.com", bob.getAddress().getValue());
        assertEquals("smtp", bob.getChannel());
        assertEquals("Nokia-6800", bob.getDeviceName());
        assertEquals("Default type has been assigned", RecipientType.TO, bob.getRecipientType());
        Recipient dave = recipients[2];
        assertEquals("dave@volantis.com", dave.getAddress().getValue());
        assertEquals("smtp", dave.getChannel());
        assertEquals("Samsung-D700", dave.getDeviceName());
        assertEquals("Default type has been assigned", RecipientType.TO, dave.getRecipientType());
    }
