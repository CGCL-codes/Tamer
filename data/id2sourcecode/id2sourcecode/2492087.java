                public void run() {
                    Channel channel = channelService.getChannel(channelName);
                    DataManager dataManager = AppContext.getDataManager();
                    ClientSession session = (ClientSession) dataManager.getBinding(sessionKey);
                    channel.join(session);
                    channel.leave(session);
                }
