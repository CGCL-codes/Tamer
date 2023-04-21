    public static List<String> getChannelsForContributionId(String contributionId) {
        List<String> returnValue = new ArrayList<String>();
        List<String> currentList = getContributionChannels().get(contributionId);
        if (currentList != null) {
            returnValue.addAll(currentList);
        }
        return returnValue;
    }
