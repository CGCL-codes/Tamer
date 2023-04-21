    public void testSenderIsDeserializedCorrectly() throws Exception {
        InputStream in = new ByteArrayInputStream(SEND_REQUEST_BASIC_WITH_SENDER.getBytes(UTF_8));
        SendRequest request = parser.readSendRequest(in);
        final Recipient[] recipients = request.getRecipients();
        assertEquals(1, recipients.length);
        Recipient alice = recipients[0];
        assertEquals("alice@volantis.com", alice.getAddress().getValue());
        assertEquals("smtp", alice.getChannel());
        assertEquals("Nokia-6600", alice.getDeviceName());
        assertEquals("Default type has been assigned", RecipientType.TO, alice.getRecipientType());
        assertEquals("Hello", request.getMessage().getSubject());
        assertEquals(new URL("http://localhost:8080/volantis/welcome/welcome.xdime"), request.getMessage().getURL());
        assertEquals("eve@volantis.com", request.getSender().getSMTPAddress().getValue());
    }
