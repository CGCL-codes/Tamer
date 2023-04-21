            public void run() {
                Channel channel = channelService.getChannel(name);
                dataService.removeObject(channel);
            }
