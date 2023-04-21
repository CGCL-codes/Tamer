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
