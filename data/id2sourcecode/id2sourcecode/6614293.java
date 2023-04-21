    public void addLink(String routerUuid, Link link) throws ConnectingException {
        if (UuidHelper.isUuid(routerUuid)) {
            String seg = UuidHelper.getSegmentFromEndpointNameOrEndpointUuid(routerUuid);
            RoutingAlgorithm algorithm = routingAlgorithms.get(seg);
            if (algorithm == null) {
                throw new ConnectingException("Router is not attached to segment: " + seg);
            }
            RouterSegment rs = segmentMapping.get(seg);
            rs.setTimestamp(0);
            link.setDestinationUuid(routerUuid);
            links.put(link.getLinkId(), link);
            if ((link.getChannel() != null) && link.getChannel().isDefaultGw()) {
                defaultGw = link;
                logger.debug("Setting defaultgw " + link);
            }
            algorithm.publishLink(link);
        } else {
            link.setDestinationUuid(routerUuid);
            links.put(link.getLinkId(), link);
            routingAlgorithms.get(routerUuid).publishLink(link);
        }
        logger.debug(getCOOSInstanceName() + ": Adding link: " + link);
    }
