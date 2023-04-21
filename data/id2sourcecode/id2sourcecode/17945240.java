    public void testSendRequestBasicWithEmbeddedContentDeserilization() throws Exception {
        InputStream in = new ByteArrayInputStream(SEND_REQUEST_BASIC_EMBEDDED_CONTENT.getBytes(UTF_8));
        SendRequest request = parser.readSendRequest(in);
        assertEquals(1, request.getRecipients().length);
        Recipient recipient = request.getRecipients()[0];
        assertEquals("alice@volantis.com", recipient.getAddress().getValue());
        assertEquals("smtp", recipient.getChannel());
        assertEquals("Nokia-6600", recipient.getDeviceName());
        assertEquals("Default type has been assigned", RecipientType.TO, recipient.getRecipientType());
        assertEquals(MESSAGE, request.getMessage().getContent());
        assertNotNull("default sender", request.getSender());
        assertNull(request.getSender().getSMTPAddress());
        assertNull(request.getSender().getMSISDN());
    }
