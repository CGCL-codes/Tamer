    @Test
    public void testApp() throws MessagingException, InterruptedException {
        msgBody1 = "Some message body 1";
        msgBody2 = "Some message body 2";
        msgBody3 = "Some message body 3";
        msgReceived1 = false;
        msgReceived2 = false;
        msgReceived3 = false;
        final String channelName = "testChannel-sdfq468_afdsg946";
        Settings settings1 = new Settings();
        settings1.setSetting(Setting.pubsub, "jms");
        settings1.setSetting(Setting.jms_port, "61616");
        settings1.setSetting(Setting.jms_username, "user1");
        settings1.setSetting(Setting.jms_password, "password1");
        Settings settings2 = new Settings();
        settings2.setSetting(Setting.pubsub, "jms");
        settings2.setSetting(Setting.jms_port, "61616");
        settings2.setSetting(Setting.jms_username, "user2");
        settings2.setSetting(Setting.jms_password, "password2");
        PubSubManager pubSubManager1 = PubSubFactory.createPubSubManager(settings1);
        PubSubManager pubSubManager2 = PubSubFactory.createPubSubManager(settings2);
        assertNotNull(pubSubManager1.getId());
        try {
            pubSubManager2.addMessageListener(new MessageListener() {

                public void processMessage(MessageEvent messageEvent) {
                    String payload = messageEvent.getMessage().getPayload();
                    log.debug("Received message: " + payload);
                    if (payload.equals(msgBody1)) {
                        msgReceived1 = true;
                        messageChannelName = messageEvent.getMessage().getChannelName();
                    }
                    if (payload.equals(msgBody2)) {
                        msgReceived2 = true;
                    }
                    if (payload.equals(msgBody3)) {
                        msgReceived3 = true;
                    }
                }
            });
            pubSubManager1.createChannel(new Channel(channelName));
            pubSubManager2.subscribe(channelName);
            pubSubManager1.publish(new PubSubMessage(channelName, msgBody1));
            pubSubManager2.publish(new PubSubMessage(channelName, msgBody2));
            Thread.sleep(200);
            pubSubManager2.unsubscribe(channelName);
            pubSubManager1.publish(new PubSubMessage(channelName, msgBody3));
            Thread.sleep(200);
            assertTrue("Message 1 received", msgReceived1);
            assertTrue("Message 2 received", msgReceived2);
            assertFalse("Message 3 received", msgReceived3);
            assertEquals("Message1 channel name", channelName, messageChannelName);
        } finally {
            pubSubManager1.close();
            pubSubManager2.close();
        }
    }
