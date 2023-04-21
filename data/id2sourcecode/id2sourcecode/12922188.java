        public ClientChannel getChannel(String channel) {
            ClientChannel c = channels.get(channel);
            if (c == null) {
                throw new ObjectNotFoundException("Player " + player + " not join to channel " + channel);
            }
            return c;
        }
