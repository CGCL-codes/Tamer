        protected void handleInput(IRCWindow window, String[] tokens) {
            Channel channel = window.getChannel();
            if (channel == null) {
                window.insertDefault("You're not in a channel");
            } else {
                window.insertDefault("Topic for " + channel.getName() + ": " + channel.getTopic() + "\nSet by " + channel.getTopicSetter() + " on " + DateUtils.getTime(channel.getTopicSetTime()));
            }
        }
