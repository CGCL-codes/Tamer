    private static Collection<NetworkLinkDescriptor> getContainedTypeModelFacetLinks_Channel_4003(Network container) {
        LinkedList<NetworkLinkDescriptor> result = new LinkedList<NetworkLinkDescriptor>();
        for (Iterator<?> links = container.getChannels().iterator(); links.hasNext(); ) {
            EObject linkObject = (EObject) links.next();
            if (false == linkObject instanceof Channel) {
                continue;
            }
            Channel link = (Channel) linkObject;
            if (ChannelEditPart.VISUAL_ID != NetworkVisualIDRegistry.getLinkWithClassVisualID(link)) {
                continue;
            }
            Node dst = link.getTarget();
            Node src = link.getSource();
            result.add(new NetworkLinkDescriptor(src, dst, link, NetworkElementTypes.Channel_4003, ChannelEditPart.VISUAL_ID));
        }
        return result;
    }
