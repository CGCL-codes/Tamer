    @Test
    public void testPublishWithoutEcho() throws IOException, MessagingException, InterruptedException {
        String channelName1 = "channel_test_g3d443f_002";
        final String payload1 = "message1";
        final String payload2 = "message2";
        psm1MsgCount = 0;
        psm2MsgCount = 0;
        msg1Received = false;
        msg2Received = false;
        Properties props1 = loadPubSubManager1Properties();
        Properties props2 = loadPubSubManager2Properties();
        props1.setProperty("xmpp_publish_echo_enabled", "false");
        props2.setProperty("xmpp_publish_echo_enabled", "false");
        pubSubManager1 = PubSubFactory.createPubSubManager(props1);
        pubSubManager2 = PubSubFactory.createPubSubManager(props2);
        pubSubManager1.addMessageListener(new MessageListener() {

            public void processMessage(MessageEvent messageEvent) {
                psm1MsgCount++;
                log.debug("pubsubmanager1 received: " + messageEvent.getMessage().getPayload());
                if (messageEvent.getMessage().getPayload().equals(payload1)) {
                    msg1Received = true;
                    try {
                        pubSubManager1.publish(new PubSubMessage(messageEvent.getMessage().getChannelName(), payload2));
                    } catch (MessagingException e) {
                        fail();
                    }
                }
            }
        });
        pubSubManager2.addMessageListener(new MessageListener() {

            public void processMessage(MessageEvent messageEvent) {
                psm2MsgCount++;
                log.debug("pubsubmanager2 received: " + messageEvent.getMessage().getPayload());
                if (messageEvent.getMessage().getPayload().equals(payload2)) {
                    msg2Received = true;
                }
            }
        });
        pubSubManager1.createChannel(new Channel(channelName1));
        pubSubManager1.subscribe(channelName1);
        pubSubManager2.subscribe(channelName1);
        pubSubManager2.publish(new PubSubMessage(channelName1, payload1));
        Thread.sleep(200);
        pubSubManager1.unsubscribe(channelName1);
        pubSubManager2.unsubscribe(channelName1);
        pubSubManager1.deleteChannel(channelName1);
        pubSubManager1.close();
        pubSubManager2.close();
        assertTrue("Msg 1 received", msg1Received);
        assertTrue("Msg 2 received", msg2Received);
        assertEquals("Number of messages psm1 received", 1, psm1MsgCount);
        assertEquals("Number of messages psm2 received", 1, psm2MsgCount);
    }
