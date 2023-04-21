    @Test
    public void testPublish1() throws MessagingException, InterruptedException, IOException {
        msgBody1 = "<bla>Some message body 1</bla>";
        msg1ReceivedCounter = 0;
        msgBody2 = "Some message body 2";
        msgBody3 = "Some message body 3";
        receivedMsgChannelName = "";
        msg1Received = false;
        msg2Received = false;
        final String channelName1 = "testChannel-testPublish1-1";
        final String channelName2 = "testChannel-testPublish1-2";
        Properties props1 = loadPubSubManager1Properties();
        Properties props2 = loadPubSubManager2Properties();
        pubSubManager1 = PubSubFactory.createPubSubManager(props1);
        pubSubManager2 = PubSubFactory.createPubSubManager(props2);
        pubSubManager1.addMessageListener(new MessageListener() {

            public void processMessage(MessageEvent messageEvent) {
                PubSubMessage message = messageEvent.getMessage();
                if (message.getPayload().equals(msgBody1)) {
                    fail("Message 1 received that should not be received.");
                }
            }
        });
        pubSubManager2.addMessageListener(new MessageListener() {

            public void processMessage(MessageEvent messageEvent) {
                PubSubMessage message = messageEvent.getMessage();
                String messageText = message.getPayload();
                if (messageText.equals(msgBody1)) {
                    msg1ReceivedCounter++;
                    msg1Received = true;
                    assertEquals("Message1 channel name.", message.getChannelName(), channelName1);
                }
                if (messageText.equals(msgBody2)) {
                    msg2Received = true;
                }
                if (messageText.equals(msgBody3)) {
                    fail("Message3 received.");
                }
            }
        });
        pubSubManager1.createChannel(new Channel(channelName1));
        assertTrue("Channel 1 created.", pubSubManager1.isChannel(channelName1));
        pubSubManager1.createChannel(new Channel(channelName2));
        assertTrue(pubSubManager1.isChannel(channelName2));
        pubSubManager2.subscribe(channelName1);
        pubSubManager1.publish(new PubSubMessage(channelName1, msgBody1));
        pubSubManager1.publish(new PubSubMessage(channelName2, msgBody1));
        pubSubManager2.publish(new PubSubMessage(channelName1, msgBody2));
        Thread.sleep(200);
        pubSubManager2.unsubscribe(channelName1);
        pubSubManager1.publish(new PubSubMessage(channelName1, msgBody3));
        Thread.sleep(200);
        pubSubManager1.deleteChannel(channelName1);
        assertFalse(pubSubManager1.isChannel(channelName1));
        pubSubManager1.deleteChannel(channelName2);
        assertFalse(pubSubManager1.isChannel(channelName2));
        pubSubManager1.close();
        pubSubManager2.close();
        assertTrue("Message 1 received", msg1Received);
        assertEquals(msg1ReceivedCounter, 1);
        if (pubSubManager2.getSetting(Setting.xmpp_publish_echo_enabled).equals(Settings.TRUE)) {
            assertTrue("Message 2 received", msg2Received);
            pubSubManager1.setSetting(Setting.xmpp_publish_echo_enabled, Settings.TRUE);
        } else {
            assertFalse("Message 2 received", msg2Received);
        }
    }
