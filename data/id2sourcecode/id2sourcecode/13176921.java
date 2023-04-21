    @Override
    public void contributionThumbprintsUpdated(IContribution contribution) {
        if (contribution != null && contribution.getId() != null) {
            for (String channelName : ChannelCacheEventListener.getChannelsForContributionId(contribution.getId())) {
                ChannelCacheController.getChannelCache().remove(channelName);
            }
        }
    }
