            public void receiveEvent(IRCEvent e) {
                JoinCompleteEvent jce = (JoinCompleteEvent) e;
                jce.getChannel().say("Hello from JerkLib!");
            }
