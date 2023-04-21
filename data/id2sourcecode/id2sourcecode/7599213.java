            public AbstractAction getAction(User user, User newUser) {
                ClientTransceiver clientTransceiver = new ClientTransceiver(user.getNetworkChannel());
                clientTransceiver.addReceiver(user.getNetworkChannel());
                ChatMessageAction action = new ChatMessageAction(message, chatChannel.getChannelId(), userId.longValue(), newUser.getName());
                action.setTransceiver(clientTransceiver);
                return action;
            }
