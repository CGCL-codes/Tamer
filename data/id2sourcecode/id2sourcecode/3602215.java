        public void deliver(Client from, Client to, Message message) {
            if (message.getChannel() != null && membersChannel.equals(message.getChannel())) {
                Object data = message.getData();
                if (data == null) return;
                if (data.getClass().isArray()) onUserListRefreshed((Object[]) data); else if (data instanceof Map) onMessageReceived(from, (Map<String, Object>) data);
            }
        }
