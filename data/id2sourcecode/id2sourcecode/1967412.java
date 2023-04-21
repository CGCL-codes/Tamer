    public static void loadSubordinates(String contributionId, List<IPopulationRule> populationRules) throws ContributionPropertyNotFoundException {
        if (contributionId != null && populationRules != null) {
            for (IPopulationRule populationRule : populationRules) {
                String channelName = getChannelName(contributionId, populationRule);
                if (!ChannelCacheController.isKeyInCache(channelName)) {
                    IMessage message = new Message();
                    IMessageData messageData = new MessageData(new PreloadComponentContributionsMessage(contributionId, populationRule, populationRules));
                    message.setMessageData(messageData);
                    message.setPriority(MessagePriority.MEDIUM);
                    publisher.sendMessage(message);
                    if (log.isDebugEnabled()) {
                        log.debug("load subordinates message sent");
                    }
                }
            }
        }
    }
