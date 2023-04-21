    public static void preloadChannel(String contributionId, IPopulationRule populationRule, List<IPopulationRule> populationRules) throws ContributionPropertyNotFoundException {
        String channelName = getChannelName(contributionId, populationRule);
        if (!ChannelCacheController.isKeyInCache(channelName)) {
            List<IContribution> contributions = ChannelCacheController.getContributionList(channelName);
            for (IContribution contribution : contributions) {
                for (IPopulationRule pr : populationRules) {
                    IMessage message = new Message();
                    IMessageData messageData = new MessageData(new PreloadComponentContributionsMessage(contribution.getId(), pr, populationRules));
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
