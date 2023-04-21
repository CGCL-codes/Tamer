            public void receiveEvent(IRCEvent e) {
                if (e.getType() == Type.MOTD) {
                    MotdEvent me = (MotdEvent) e;
                    System.out.println(me.getMotdLine());
                } else {
                    JoinCompleteEvent je = (JoinCompleteEvent) e;
                    je.getChannel().say("Yay tasks!");
                }
            }
