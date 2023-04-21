    private void publishAndCleanUpAfterLinkRemoval(Link link) {
        link.setCost(LinkCost.MAX_VALUE);
        routingAlgorithms.get(UuidHelper.getSegmentFromEndpointNameOrEndpointUuid(link.getDestinationUuid())).publishLink(link);
        for (Map<String, Link> routingTable : routingTables.values()) {
            for (String uuidKey : routingTable.keySet()) {
                Link l = routingTable.get(uuidKey);
                if ((l != null) && l.equals((link))) {
                    routingTable.remove(uuidKey);
                    for (String aliasKey : aliasTable.keySet()) {
                        String uuid = aliasTable.get(aliasKey);
                        if (uuidKey.equals(uuid)) {
                            removeAlias(aliasKey);
                        }
                    }
                }
            }
            if (COOS != null) COOS.removeChannel(link.getDestinationUuid());
        }
        for (String aliasKey : aliasTable.keySet()) {
            String uuid = aliasTable.get(aliasKey);
            if (link.getDestinationUuid().equals(uuid)) {
                removeAlias(aliasKey);
            }
        }
        if ((link.getChannel() != null) && !link.getChannel().isDefaultGw()) {
            links.remove(link.getLinkId());
        }
    }
