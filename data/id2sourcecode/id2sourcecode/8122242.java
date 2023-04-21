        protected void handleInput(IRCWindow window, String[] tokens) {
            Channel channel = window.getChannel();
            String privNick = window.getNick();
            Session session = window.getSession();
            String ourNick = session.getNick();
            String input = (char) 1 + "ACTION " + tokens[0] + (char) 1;
            if (channel != null) {
                System.out.println("channel not null");
                channel.say(input);
                window.insertDefault("* " + ourNick + " " + tokens[0]);
            } else if (privNick != null) {
                System.out.println("trying other");
                session.sayPrivate(privNick, input);
                window.insertDefault("* " + ourNick + " " + tokens[0]);
            }
        }
