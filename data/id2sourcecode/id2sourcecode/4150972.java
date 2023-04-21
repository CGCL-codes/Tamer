            public void run() {
                Channel channel = getChannel(channelName);
                for (String user : users) {
                    ClientSession session = (ClientSession) dataService.getBinding(user);
                    channel.join(session);
                }
            }
