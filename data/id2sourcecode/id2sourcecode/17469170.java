        public void fireMessageEvent(MessageEvent messageEvent) {
            Iterator<MessageListener> itor = listeners.keySet().iterator();
            while (itor.hasNext()) {
                MessageListener messageListener = itor.next();
                List<String> channels = listeners.get(messageListener);
                if (channels != null) {
                    if (channels.contains(messageEvent.getMessage().getChannelName())) {
                        messageListener.processMessage(messageEvent);
                    }
                } else {
                    messageListener.processMessage(messageEvent);
                }
            }
        }
