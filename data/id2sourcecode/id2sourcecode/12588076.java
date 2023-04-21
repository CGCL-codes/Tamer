        @Override
        public void run() {
            for (WeakReference<MessageListener> ref : set) {
                MessageListener l = ref.get();
                if (l != null) {
                    l.messagePosted(message);
                }
            }
            if (!message.isRemote()) {
                if (messageBusClient != null && message.getChannel() != MessageChannel.SYSTEM) {
                    messageBusClient.sendRemoteMessage(message);
                }
            }
        }
