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
