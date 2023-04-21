    private static Collection<NetworkLinkDescriptor> getOutgoingTypeModelFacetLinks_Channel_4003(Node source) {
        Network container = null;
        for (EObject element = source; element != null && container == null; element = element.eContainer()) {
            if (element instanceof Network) {
                container = (Network) element;
            }
        }
        if (container == null) {
            return Collections.emptyList();
        }
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
            if (src != source) {
                continue;
            }
            result.add(new NetworkLinkDescriptor(src, dst, link, NetworkElementTypes.Channel_4003, ChannelEditPart.VISUAL_ID));
        }
        return result;
    }
