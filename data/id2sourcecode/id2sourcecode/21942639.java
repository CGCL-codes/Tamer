    @Test
    public void testWriteValue() {
        value.setSessionId(SAMPLE_ATTR);
        ByteBuffer buffer = IOUtil.createBuffer(value.getSize());
        value.writeValue(buffer);
        buffer.flip();
        SessionIdAttributeValue readValue = new SessionIdAttributeValue();
        readValue.readValue(buffer);
        assertNotSame(readValue.getValue(), SAMPLE_ATTR);
        assertEquals("read after write does not match", SAMPLE_ATTR, readValue.getValue());
    }
