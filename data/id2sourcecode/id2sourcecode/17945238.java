    public void testSendRequestBasicDeserialization() throws Exception {
        InputStream in = new ByteArrayInputStream(SEND_REQUEST_BASIC.getBytes(UTF_8));
        SendRequest request = parser.readSendRequest(in);
        assertEquals(1, request.getRecipients().length);
        Recipient recipient = request.getRecipients()[0];
        assertEquals("alice@volantis.com", recipient.getAddress().getValue());
        assertEquals("smtp", recipient.getChannel());
        assertEquals("Nokia-6600", recipient.getDeviceName());
        assertEquals("Default type has been assigned", RecipientType.TO, recipient.getRecipientType());
        assertEquals("Hello", request.getMessage().getSubject());
        assertEquals(new URL("http://localhost:8080/volantis/welcome/welcome.xdime"), request.getMessage().getURL());
        assertNotNull("default sender", request.getSender());
        assertNull(request.getSender().getSMTPAddress());
        assertNull(request.getSender().getMSISDN());
    }
