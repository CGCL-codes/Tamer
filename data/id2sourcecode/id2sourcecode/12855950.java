    public static List<NetworkLinkDescriptor> getContainedLinks(View view) {
        switch(NetworkVisualIDRegistry.getVisualID(view)) {
            case NetworkEditPart.VISUAL_ID:
                return getNetwork_1000ContainedLinks(view);
            case NodeEditPart.VISUAL_ID:
                return getNode_2001ContainedLinks(view);
            case ChannelEditPart.VISUAL_ID:
                return getChannel_4003ContainedLinks(view);
        }
        return Collections.emptyList();
    }
