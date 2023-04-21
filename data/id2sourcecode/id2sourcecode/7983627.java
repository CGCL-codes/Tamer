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
