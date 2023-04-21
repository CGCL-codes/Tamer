    public void testSetChannel() {
        try {
            Channel channel = new Channel("www.italia1.it", "Italia1");
            programTest.setChannel(channel);
            assertEquals(channel, programTest.getChannel());
        } catch (ParseException e) {
            fail("Exception");
        }
    }
