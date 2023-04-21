    private static String getChannelName(String contributionId, IPopulationRule populationRule) throws ContributionPropertyNotFoundException {
        StringBuffer returnValue = new StringBuffer();
        String relationship = populationRule.getPropertyValue(ReservedTypes.POPULATION_RULE_RELATIONSHIP_TO_SUPERIOR);
        String subordinateType = populationRule.getPropertyValue(ReservedTypes.POPULATION_RULE_SUBORDINATE_TYPE);
        returnValue.append("/").append(Channels.CONTRIBUTIONS).append("/").append(Channels.RELATIONSHIP).append("/").append(contributionId).append("/").append(relationship).append("/").append(subordinateType).append("/").append(Channels.ADD);
        return returnValue.toString();
    }
