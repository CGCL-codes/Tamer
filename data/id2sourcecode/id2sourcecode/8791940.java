            public void run() {
                if (chanEvent.getType().equals(IRCEvent.Type.PRIVATE_MESSAGE)) {
                    IRCWindow window = sessionContainer.getPrivateMessageWindow(chanEvent.getNick(), e.getSession());
                    window.insertMsg(chanEvent.getNick(), chanEvent.getMessage());
                } else {
                    IRCWindow window = sessionContainer.findWindowByChannel(chanEvent.getChannel());
                    window.insertMsg(chanEvent.getNick(), chanEvent.getMessage());
                }
            }
