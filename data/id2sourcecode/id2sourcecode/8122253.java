        public void receiveInput(IRCWindow window, String input) {
            if (window == null) {
                throw new IllegalArgumentException("Junk not recognized");
            }
            Channel channel = window.getChannel();
            String privNick = window.getNick();
            Session session = window.getSession();
            String ourNick = session.getNick();
            if (channel != null) {
                System.out.println("channel not null");
                channel.say(input);
                window.insertMsg(ourNick, input);
            } else if (privNick != null) {
                System.out.println("trying other");
                session.sayPrivate(privNick, input);
                window.insertMsg(ourNick, input);
            }
        }
